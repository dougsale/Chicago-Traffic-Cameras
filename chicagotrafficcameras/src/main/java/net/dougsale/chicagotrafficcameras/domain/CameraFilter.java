/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain;

public interface CameraFilter {//extends Predicate<Camera> {

	/**
	 * Returns true if the camera passes this filter.
	 * @param camera
	 * @return true if the camera passes
	 */
	boolean accept(Camera camera);
}