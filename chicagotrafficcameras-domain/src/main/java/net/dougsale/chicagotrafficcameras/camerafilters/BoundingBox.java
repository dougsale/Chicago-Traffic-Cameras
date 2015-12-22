/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import net.dougsale.chicagotrafficcameras.Cameras;
import net.dougsale.chicagotrafficcameras.Route.Step;
import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.Location;

/**
 * 
 * @author dsale
 *
 */
public class BoundingBox implements CameraFilter {

	private double minLat;
	private double maxLat;
	private double minLng;
	private double maxLng;						
	
	/**
	 * BoundingBox filters cameras that aren't located in a box
	 * defined the start and the stop of the step parameter and
	 * a padding value applied equally in all 4 cardinal route.
	 * Note that the padding is in degrees and thus isn't equal in absolute
	 * distance between latitude and longitude.  Over small padding values
	 * this is considered negligible.
	 * @param step the step of the route route
	 * @param padding allowed variance, in degrees
	 */
	public BoundingBox(Step step, double padding) {
		notNull(step, "invalid parameter: step=" + step);
		isTrue(padding >= 0.0, "invalid parameter: padding=" + padding + " (must be >= 0.0)");
		
		Location start = step.start;
		Location end = step.end;
		
		minLat = Math.min(start.latitude, end.latitude) - padding;
		maxLat = Math.max(start.latitude, end.latitude) + padding;
		minLng = Math.min(start.longitude, end.longitude) - padding;
		maxLng = Math.max(start.longitude, end.longitude) + padding;						
	}
	
	@Override
	public Cameras filter(Cameras cameras) {
		Cameras filtered = new Cameras();
//		for (Camera camera : cameras.get(comparator))
		return null;
	}

	@Override
	public boolean accept(Camera camera) {
		return inBounds(camera);
	}

	public boolean inBounds(Camera camera) {
		notNull(camera, "invalid parameter: camera=" + camera);
		boolean inBounds = camera.location.latitude >= minLat && camera.location.latitude <= maxLat
				&& camera.location.longitude >= minLng && camera.location.longitude <= maxLng;
		return inBounds;
	}
}