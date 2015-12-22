/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Arrays;

import net.dougsale.chicagotrafficcameras.Route.Step;
import net.dougsale.chicagotrafficcameras.camerafilters.BoundingBox;
import net.dougsale.chicagotrafficcameras.camerafilters.CameraFilterFactory;
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
	
	private Cameras cameras;
	private CameraFilterFactory[] filterFactories;
	
	public CameraLocator(Cameras cameras, CameraFilterFactory...filterFactories) {
		notNull(cameras, "Invalid parameter: cameras=" + cameras);
		this.cameras = cameras;

		notEmpty(filterFactories, "Invalid parameter: filterFactories=" + filterFactories);
		this.filterFactories = Arrays.copyOf(filterFactories, filterFactories.length);
	}
	
	public Cameras newLocate(Route route) {
		notNull(route, "invalid parameter: route=" + route);
		
		for (CameraFilterFactory filterFactory : filterFactories)
			filterFactory.setRoute(route);

		Cameras located = cameras;
		
		for (Step step : route.steps)
			for (CameraFilterFactory filterFactory : filterFactories)
					located = filterFactory.getCameraFilter(step).filter(located);
		
		return located;
	}
	
	public CameraLocator(Cameras cameras) {
		notNull(cameras, "Invalid parameter: cameras=" + cameras);
		this.cameras = cameras;
	}
	
	public Cameras locate(Route route) {
		notNull(route, "invalid parameter: route=" + route);
		
		StreetMatcherFactory smFactory = new StreetMatcherFactory();
		smFactory.setRoute(route);
		
		Cameras located = new Cameras();
		
		for (Step step : route.steps) {

			BoundingBox box = new BoundingBox(step, BOX_PADDING);
			StreetMatcher street = smFactory.getStreetMatcher(step);

			// TODO the following would be a good candidate for stream processing

			// TODO determine smallest range of box dimensions (either lat or long) and
			// then get a navigable set from cameras and only iterate over that subrange
			
			for (RedLightCamera camera : cameras.get(RedLightCamera.class)) {
				if (box.accept(camera)  && street.accept(camera)) {
					located.add(camera);
				}
			}
			
			for (SpeedCamera camera : cameras.get(SpeedCamera.class)) {
				if (box.accept(camera)  && street.accept(camera)) {
					located.add(camera);
				}
			}
		}
		
		return located;
	}
}