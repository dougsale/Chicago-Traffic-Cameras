package net.dougsale.chicagotrafficcameras.camerafilters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dougsale.chicagotrafficcameras.CameraFilter;
import net.dougsale.chicagotrafficcameras.Directions.Step;
import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.Location;

public class BoundingBox implements CameraFilter {

	private static final Logger logger = LoggerFactory.getLogger(BoundingBox.class);

	private double minLat;
	private double maxLat;
	private double minLng;
	private double maxLng;						
	
	public BoundingBox(Step step, double boxPadding) {
		Location start = step.start;
		Location end = step.end;
		
		minLat = Math.min(start.latitude, end.latitude) - boxPadding;
		maxLat = Math.max(start.latitude, end.latitude) + boxPadding;
		minLng = Math.min(start.longitude, end.longitude) - boxPadding;
		maxLng = Math.max(start.longitude, end.longitude) + boxPadding;						
	}
	
	public boolean inBounds(Camera camera) {
		boolean inBounds = camera.location.latitude >= minLat && camera.location.latitude <= maxLat
				&& camera.location.longitude >= minLng && camera.location.longitude <= maxLng;
				
		logger.debug("inBounds = {} for camera ({})", inBounds, camera);
		
		return inBounds;
	}

	@Override
	public boolean accept(Camera camera) {
		return inBounds(camera);
	}
}