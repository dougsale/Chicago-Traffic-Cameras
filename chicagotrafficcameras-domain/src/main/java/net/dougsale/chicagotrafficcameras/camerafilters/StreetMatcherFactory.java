/**
 * 
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dougsale.chicagotrafficcameras.Directions;
import net.dougsale.chicagotrafficcameras.Directions.Step;
import net.dougsale.chicagotrafficcameras.domain.Camera;

/**
 * @author dsale
 *
 */
public class StreetMatcherFactory {

	private static final Logger logger = LoggerFactory.getLogger(StreetMatcherFactory.class);
	
	static final StreetMatcher streetMatcherAlways =
			new StreetMatcher() {
				@Override
				public boolean accept(Camera camera) { return match(camera); }
				@Override
				public boolean match(Camera camera) { return true; }			
			};

	static final StreetMatcher streetMatcherNever =
			new StreetMatcher() {
				@Override
				public boolean accept(Camera camera) { return match(camera); }
				@Override
				public boolean match(Camera camera) { return true; }			
			};

	private Directions directions;
	private Map<Step, String> streetForStep;
	
	public StreetMatcherFactory(Directions directions) {
		notNull(directions);
		this.directions = directions;
	}
	
	public StreetMatcher get(Step step) {
		notNull(step);
		
		String street = getStreetForStep(step);
		if (street != null) {
			return new DefaultStreetMatcher(street);
		} else {
			if (streetForStep.containsKey(step)) {
				return streetMatcherAlways;
			} else {
				return streetMatcherNever;
			}
		}
	}

	/**
	 * Returns the street driven for the given step.
	 * Also handles lazy initialization/processing of the directions. 
	 * @param step
	 * @return the street driven for the given step
	 */
	String getStreetForStep(Step step) {
		if (streetForStep == null)
			streetForStep = createStreetForStepMapping(directions);
		
		return streetForStep.get(step);
	}

	/**
	 * Processes the Google-supplied directions and determines the street being navigated
	 * in each step of the directions.
	 * @param directions
	 * @return a mapping between each steps of the directions and the street being navigated
	 */
	Map<Step, String> createStreetForStepMapping(Directions directions) {
		
		Map<Step, String> mapping = new HashMap<>();
		
		// retrieve the starting street
		String street = extractStreetFromAddress(directions.startAddress);

		for (Step step : directions.steps) {
			street = extractStreetFromInstructions(step.instructions, street);
			
			if (street == null) {
				logger.warn("Could not determine street utilized in step in directions: step={}; directions={}", step, directions);
			}
			
			mapping.put(step, street);
		}

		return mapping;
	}
	
	String extractStreetFromAddress(String address) {
		Matcher matcher = addressPattern.matcher(address);
		return matcher.matches()? matcher.group(2) : null;
	}

	// example start address: 716 S Central Park Ave, Chicago, IL 60624, USA
	private static final Pattern addressPattern =
		Pattern.compile(// 1 = addressNumber, 2 = street, 3 = city, 4 = state, 5 = zip, 6 = country
			"^\\s*(\\d+)\\s+([^,]+),\\s*([^,]+),\\s*([^\\s]+)\\s+([^,]+),\\s*([^\\s]+)\\s*$",
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
