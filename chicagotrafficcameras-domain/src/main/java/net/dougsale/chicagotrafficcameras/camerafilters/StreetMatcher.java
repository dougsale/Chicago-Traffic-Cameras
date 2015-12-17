package net.dougsale.chicagotrafficcameras.camerafilters;

import net.dougsale.chicagotrafficcameras.CameraFilter;
import net.dougsale.chicagotrafficcameras.domain.Camera;

public interface StreetMatcher extends CameraFilter {

	boolean match(Camera camera);
}