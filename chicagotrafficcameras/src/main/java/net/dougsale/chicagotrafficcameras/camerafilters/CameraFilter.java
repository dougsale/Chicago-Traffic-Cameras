/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import net.dougsale.chicagotrafficcameras.domain.Camera;

public interface CameraFilter {//extends Predicate<Camera> {

	/**
	 * Returns true if the camera passes this filter.
	 * @param camera
	 * @return true if the camera passes
	 */
	boolean accept(Camera camera);
	
	/**
	 * Maps the predicate method to the accept method.
	 */
//	@Override
//	default boolean test(Camera camera) {
//		return accept(camera);
//	}
}