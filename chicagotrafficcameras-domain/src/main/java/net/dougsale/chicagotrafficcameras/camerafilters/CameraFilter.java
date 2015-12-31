/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import net.dougsale.chicagotrafficcameras.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Camera;

public interface CameraFilter {

	/**
	 * Returns true if the camera is accepted by this filter.
	 * @param camera
	 * @return true if the camera is accepted
	 */
	boolean accept(Camera camera);
	
	/**
	 * Filters the input cameras, returning those accepted
	 * by this filter.
	 * @param cameras
	 * @returns cameras accepted by this filter
	 */
	Cameras filter(Cameras cameras);	
}