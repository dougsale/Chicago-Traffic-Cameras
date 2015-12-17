package net.dougsale.chicagotrafficcameras.camerafilters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dougsale.chicagotrafficcameras.CameraFilter;
import net.dougsale.chicagotrafficcameras.Directions.Step;
import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

public class DefaultStreetMatcher implements CameraFilter, StreetMatcher {

	private static final Logger logger = LoggerFactory.getLogger(DefaultStreetMatcher.class);
	
	private static final Pattern streetExtractorPattern =
			Pattern.compile(
					"^\\s*\\d+\\s+(?:[NSEW]\\s+)?(.+?)(?:\\s+Ave)?(?:\\s+St)?(?:\\s+Rd)?(?:\\s+Dr)?(?:\\s+Hwy)?(?:\\s+Blvd)?\\s*$",
					Pattern.CASE_INSENSITIVE);
	
	private String stepStreet;

	public DefaultStreetMatcher(String street) {
		this.stepStreet = street.toLowerCase();
	}

	/* (non-Javadoc)
	 * @see net.dougsale.chicagotrafficcameras.camerafilters.StreetMatcher#accept(net.dougsale.chicagotrafficcameras.domain.Camera)
	 */
	@Override
	public boolean accept(Camera camera) {
		return match(camera);
	}

	/* (non-Javadoc)
	 * @see net.dougsale.chicagotrafficcameras.camerafilters.StreetMatcher#match(net.dougsale.chicagotrafficcameras.domain.Camera)
	 */
	@Override
	public boolean match(Camera camera) {
		
		boolean match = false;
		
		if (camera instanceof SpeedCamera) {
			
			String cameraStreet = streetForAddress(((SpeedCamera) camera).address);
			match = stepStreet.contains(cameraStreet.toLowerCase());
			
		} else if (camera instanceof RedLightCamera) {
			
			for (String cameraStreet : ((RedLightCamera)camera).intersection)
				if (stepStreet.contains(cameraStreet.toLowerCase()))
					match = true;
		}
		
//		logger.debug("match = {} for step instructions ({}) and camera ({})", match, stepStreet, camera);
		
		return match;
	}
	
	String streetForAddress(String address) {
		Matcher matcher = streetExtractorPattern.matcher(address);
		return matcher.matches()? matcher.group(1) : null;
	}
}