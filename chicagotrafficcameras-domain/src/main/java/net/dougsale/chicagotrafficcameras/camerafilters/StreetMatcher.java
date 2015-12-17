package net.dougsale.chicagotrafficcameras.camerafilters;

import net.dougsale.chicagotrafficcameras.domain.Camera;

public interface StreetMatcher {

	boolean accept(Camera camera);

	boolean match(Camera camera);

}