package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.apache.commons.lang3.Validate.notEmpty;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dougsale.chicagotrafficcameras.CameraFilter;
import net.dougsale.chicagotrafficcameras.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;
import net.dougsale.chicagotrafficcameras.repository.CamerasRepository;

public class DefaultStreetMatcher implements CameraFilter, StreetMatcher {

	private static final Pattern streetExtractorPattern =
			Pattern.compile("^\\s*(?:\\d+\\s+)(?:[NSEW]\\s+)?(.*?)\\s*", Pattern.CASE_INSENSITIVE);
	
	private String stepStreet;

	public DefaultStreetMatcher(String street) {
		notEmpty(street);
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
		
		return match;
	}
	
	String streetForAddress(String address) {
		Matcher matcher = streetExtractorPattern.matcher(address);
		return matcher.matches()? matcher.group(1).replaceAll("\\s+", " ") : null;
	}
}