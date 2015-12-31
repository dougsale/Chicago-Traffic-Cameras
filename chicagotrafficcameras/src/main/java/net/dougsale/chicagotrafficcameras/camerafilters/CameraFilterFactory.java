package net.dougsale.chicagotrafficcameras.camerafilters;

import java.util.Map;

import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.Route.Step;

/**
 * Factory interface for retrieving CameraFilter instances
 * for a given Step of a Route.
 * @author dsale
 *
 */
public interface CameraFilterFactory {
	
	/**
	 * Returns CameraFilters for each Step in the Route.
	 * @param route
	 * @returns a Map of CameraFilters by Step
	 */
	Map<Step,CameraFilter> getCameraFilters(Route route);
}
