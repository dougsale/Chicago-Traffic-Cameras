/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import net.dougsale.chicagotrafficcameras.CameraFilter;
import net.dougsale.chicagotrafficcameras.domain.Camera;

/**
 * Interface for matching Camera instances by street representation.
 * @author dsale
 */
public interface StreetMatcher extends CameraFilter {
	/**
	 * Returns true if camera matches.
	 * @param camera
	 * @return true if camera matches
	 */
	boolean match(Camera camera);
}