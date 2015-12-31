/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dougsale.chicagotrafficcameras.Route;
import net.dougsale.chicagotrafficcameras.Route.Step;
import net.dougsale.chicagotrafficcameras.domain.Camera;

/**
 * StreetMatcherFactory provides appropriate StreetMatcher instances
 * for a given Step in a Route.  The Route is digested to compute
 * the appropriate street representation used in the StreetMatcher
 * instances.  In the case where the StreetMatcherFactory can't 
 * determine the street representation for a given Step, it returns
 * a StreetMatcher that always matches; false positives being
 * better than false negatives.
 * @author dsale
 */
public class StreetMatcherFactory implements CameraFilterFactory {

	private static final Logger logger = LoggerFactory.getLogger(StreetMatcherFactory.class);
	
	public static final StreetMatcher streetMatcherAlways =
		new StreetMatcher() {
			@Override
			public boolean accept(Camera camera) { return true; }			
		};

	/**
	 * Returns a StreetMatcher instance for each Step of
	 * the given Route provided.
	 * <p>
	 * Note that if the StreetMatcherFactory can't determine an
	 * appropriate street representation, the StreetMatcher returned
	 * will always match.
	 * </p>
	 * @param route
	 * @return
	 */
	@Override
	public Map<Step,CameraFilter> getCameraFilters(Route route) {
		notNull(route, "invalid parameter: route=null");
		
		Map<Step,CameraFilter> filters = new HashMap<>(route.getSteps().size());
		
		StreetMatcher matcher;
		String street = extractStreetFromAddress(route.getStartAddress());
		
		for (Step step : route.getSteps()) {
			street = extractStreetFromInstructions(step.getInstructions(), street);
			
			if (street != null) {
				matcher = new StreetMatcher(street);
			} else {
				logger.warn("Could not determine street utilized in Step of Route: step={}; route={}", step, route);
				matcher = streetMatcherAlways;
			}

			filters.put(step, matcher);
		}
		
		return filters;
	}
	
	/**
	 * 
	 * @param address
	 * @return
	 */
	String extractStreetFromAddress(String address) {
		Matcher matcher = addressPattern.matcher(address);
		if (matcher.matches()) {
			return matcher.group(1);
		} else {
			logger.warn("Failed to extract street from address: address={}", address);
			return null;
		}
	}

	// example start address: 716 S Central Park Ave, Chicago, IL 60624, USA
	private static final Pattern addressPattern =
		// if remove "?:", groups would be: 1 = addressNumber, 2 = street, 3 = city, 4 = state, 5 = zip, 6 = country
		// imposes unnecessary storage on each matcher, so just 1 group - the street
		Pattern.compile(
			"^\\s*(?:\\d+)\\s+([^,]+),\\s*(?:[^,]+),\\s*(?:[^\\s]+)\\s+(?:[^,]+),\\s*(?:[^\\s]+)\\s*$",
			Pattern.CASE_INSENSITIVE
		);

	/**
	 * Parses the instructions provided by Google and determines which street is being driven.
	 * Note that if the street can't be determined, <code>null</code> is returned.
	 * @param instructions
	 * @param priorStreet the street prior to the current instructions
	 * @return the street name or <code>null</code> if indeterminate
	 */
	String extractStreetFromInstructions(String instructions, String priorStreet) {
		
		Matcher matcher;
		String street = null;
		
		// try different matchers until a match is found.
		// once a match is found, return the street.
		// the ordering of matchers is important, as more general
		// matchers will match many of the more specific cases
		
		// instruction specifies u-turn, so on the same street
		matcher = uturnInstructionPattern.matcher(instructions);
		if (matcher.matches())
			return priorStreet;

		// instruction provides street with alternate name; return both so that either may be matched
		// note: there is no standard as to the ordering with respect to canonical name
		matcher = alternateStreetNameInstructionPattern.matcher(instructions);
		if (matcher.matches())
			return String.format("%s/%s", matcher.group(2), matcher.group(3));
		
		// a case for a specific instruction type that doesn't conform to the more general cases (see pattern)
		matcher = ontoInstructionPattern.matcher(instructions);
		if (matcher.matches())
			return matcher.group(1);
		
		// exit ramps, basically
		matcher = takeExitInstructionPattern.matcher(instructions);
		if (matcher.matches())
			return String.format("%s (exit %s)", priorStreet, matcher.group(1));
		
		// the (seemingly) most frequent instruction form (colloquially, "hang a left on archer ave")
		matcher = doubleBoldedTermInstructionPattern.matcher(instructions);
		if (matcher.matches())
			return matcher.group(2);

		// fairly frequent instruction form ("continue onto crooked st")
		matcher = singleBoldedTermInstructionPattern.matcher(instructions);
		if (matcher.matches())
			return matcher.group(1);
			
		return street;
	}

	// Example:
	//     "Make a <b>U-turn</b> at <b>W Superior St</b>"
	// Note: this is a specific case of the generalized double bolded term regex; thus it should be tried prior to it
	private static final Pattern uturnInstructionPattern =
			Pattern.compile("^[^<]*<b>u-turn</b>.*$", Pattern.CASE_INSENSITIVE);
	
	// Example:
	//     "Keep <b>left</b> to continue on <b>Historic U.S. 66 W</b>/<b>W Ogden Ave</b>"
	//     "Turn <b>right</b> onto <b>S Cicero Ave</b>/<b>Mandela Rd</b>"
	// Note: this type of instruction will be matched by either of the more generalized regex expressions, so it must be tried 1st
	private static final Pattern alternateStreetNameInstructionPattern =
			Pattern.compile("^[^<]*<b>([^<]+)</b>[^<]*<b>([^<]+)</b>/<b>([^<]+)</b>.*$");
	
	// Example:
	//     "Continue onto <b>I-290 W</b> (signs for <b>I-90</b>)"
	// Note: this type of instruction will be matched by either of the more generalized regex expressions, so it must be tried 1st
	private static final Pattern ontoInstructionPattern =
			Pattern.compile("^.*? onto [^<]*<b>([^<]+)</b>[^<]*<b>([^<]+)</b>.*$");
	
	// Example:
	//     "Take exit <b>25</b> toward <b>Kostner Ave</b>\"
	// Note: this type of instruction will be matched by either of the more generalized regex expressions, so it must be tried 1st
	private static final Pattern takeExitInstructionPattern =
			Pattern.compile("^take exit <b>([^<]+)</b>.*$", Pattern.CASE_INSENSITIVE);

	// Example:
	//     "Turn <b>left</b> onto <b>W Ogden Ave</b><div style=\"font-size:0.9em\">Destination will be on the right</div>"
	private static final Pattern doubleBoldedTermInstructionPattern =
			Pattern.compile("^[^<]*<b>([^<]+)</b>[^<]*<b>([^<]+)</b>.*$");
	
	// Example:
	//     "Continue onto <b>N Central Park Ave</b>"
	// Note that this regex will match instructions with 2 bolded terms, so it should be matched only after that regex fails
	private static final Pattern singleBoldedTermInstructionPattern =
			Pattern.compile("^[^<]*<b>([^<]+)</b>.*$");

}
