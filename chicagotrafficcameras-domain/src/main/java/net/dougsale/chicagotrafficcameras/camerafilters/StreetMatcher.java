/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dougsale.chicagotrafficcameras.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

/**
 * A CameraFilter instance that determines if a given Step of a Route
 * matches a street representation.
 * @author dsale
 */
public class StreetMatcher implements CameraFilter {

	private static final Pattern streetExtractorPattern =
			Pattern.compile("^\\s*(?:\\d+\\s+)(?:[NSEW]\\s+)?(.*?)\\s*", Pattern.CASE_INSENSITIVE);
	
	private String stepStreet;

	/**
	 * Creates a DefaultStreetmatcher instance for a given
	 * street representation.
	 * @param street
	 */
	public StreetMatcher(String street) {
		notNull(street, "invalid parameter: street=" + street);
		notEmpty(street.trim(), "invalid parameter: street=" + street);
		this.stepStreet = street.trim().toLowerCase();
	}
	
	protected StreetMatcher() {
		stepStreet = null;
	}

	@Override
	public Cameras filter(Cameras cameras) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean accept(Camera camera) {
		return match(camera);
	}

	public boolean match(Camera camera) {
		notNull(camera, "invalid parameter: camera=" + camera);
		
		boolean match = false;
		
		if (camera instanceof SpeedCamera) {
			
			String cameraStreet = streetForAddress(((SpeedCamera) camera).address);
			match = stepStreet.contains(cameraStreet.toLowerCase());
			
		} else if (camera instanceof RedLightCamera) {
			
			for (String cameraStreet : ((RedLightCamera)camera).intersection)
				if (stepStreet.contains(cameraStreet.toLowerCase()))
					match = true;
		}
		
		return match;
	}
	
	String streetForAddress(String address) {
		Matcher matcher = streetExtractorPattern.matcher(address);
		return matcher.matches()? matcher.group(1).replaceAll("\\s+", " ") : null;
	}
}