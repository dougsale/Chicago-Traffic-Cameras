package net.dougsale.chicagotrafficcameras.camerafilters;

import net.dougsale.chicagotrafficcameras.Route;
import net.dougsale.chicagotrafficcameras.Route.Step;

/**
 * Factory interface for retrieving CameraFilter instances
 * for a given Step of a Route.
 * @author dsale
 *
 */
public interface CameraFilterFactory {
	
	/**
	 * Sets the route.  Any subsequent calls to getCameraFilter(Step step)
	 * will be based on this route.
	 * @param route
	 */
	void setRoute(Route route);
	
	/**
	 * Return a CameraFilter for the given Step.
	 * @param step
	 * @return a CameraFilter instance
	 */
	CameraFilter getCameraFilter(Step step);
}
