/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dougsale.chicagotrafficcameras.Directions.Step;
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

	private static final Logger logger = LoggerFactory.getLogger(CameraLocator.class);
	
	// NOTE
	// it is possible that google and the city of chicago
	// use slightly different encoding standards with
	// regards to location (i.e., google's start and end
	// coordinates for a step in relation to chicago's
	// coordinates for its traffic cameras)
	
	// pad bounding box as cameras may lay outside the box
	// defined by the route
	// TODO determine an appropriate value
	static final double BOX_PADDING = 0.0003;
	
	// for cameras within the bounding box, ensure
	// they are within a certain distance threshold
	// from the line segment defined by the start
	// and end locations
	// TODO determine an appropriate value
	static final double DISTANCE_THRESHOLD = 0.0003;

	private Cameras allCameras;
	
	public CameraLocator(Cameras cameras) {
		this.allCameras = cameras;
	}
	
	public Cameras locate(Directions directions) {
		
		StreetMatcherFactory smFactory = new StreetMatcherFactory(directions);
		
		Cameras foundCameras = new Cameras();
		
		for (Step step : directions.steps) {

			BoundingBox box = new BoundingBox(step, BOX_PADDING);
//			StepSegment segment = new StepSegment(step, DISTANCE_THRESHOLD);
			StreetMatcher street = smFactory.get(step);

			// TODO this would be a great place to implement stream processing
			
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
