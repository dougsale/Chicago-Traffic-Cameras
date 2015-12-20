/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras;

import static org.apache.commons.lang3.Validate.notNull;

import net.dougsale.chicagotrafficcameras.Route.Step;
import net.dougsale.chicagotrafficcameras.camerafilters.BoundingBox;
import net.dougsale.chicagotrafficcameras.camerafilters.StreetMatcher;
import net.dougsale.chicagotrafficcameras.camerafilters.StreetMatcherFactory;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

/**
 * @author dsale
 *
 */
public class CameraLocator {

	// pad bounding box as cameras may lay outside the box
	// defined by the route (especially when the route is
	// almost strictly horizontal or vertical)
	// TODO determine an appropriate value
	static final double BOX_PADDING = 0.0003;
	
	private Cameras allCameras;
	
	public CameraLocator(Cameras cameras) {
		notNull(cameras, "Invalid parameter: cameras=" + cameras);
		this.allCameras = cameras;
	}
	
	public Cameras locate(Route route) {
		notNull(route, "invalid parameter: route=" + route);
		
		StreetMatcherFactory smFactory = new StreetMatcherFactory(route);
		
		Cameras foundCameras = new Cameras();
		
		for (Step step : route.steps) {

			BoundingBox box = new BoundingBox(step, BOX_PADDING);
			StreetMatcher street = smFactory.get(step);

			// TODO the following would be a good candidate for stream processing

			// TODO determine smallest range of box dimensions (either lat or long) and
			// then get a navigable set from cameras and only iterate over that subrange
			
			for (RedLightCamera camera : allCameras.get(RedLightCamera.class)) {
				if (box.accept(camera)  && street.accept(camera)) {
					foundCameras.add(camera);
				}
			}
			
			for (SpeedCamera camera : allCameras.get(SpeedCamera.class)) {
				if (box.accept(camera)  && street.accept(camera)) {
					foundCameras.add(camera);
				}
			}
		}
		
		return foundCameras;
	}
}