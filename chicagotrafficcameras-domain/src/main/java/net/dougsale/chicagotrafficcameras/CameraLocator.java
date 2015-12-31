/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import net.dougsale.chicagotrafficcameras.Route.Step;
import net.dougsale.chicagotrafficcameras.camerafilters.CameraFilter;
import net.dougsale.chicagotrafficcameras.camerafilters.CameraFilterFactory;

/**
 * CameraLocator uses filtering strategies to locate traffic cameras
 * along a given route.
 * 
 * <p>
 * <i>methinks it should not only be a container for strategies, but at some point
 * be responsible for mapping cameras to steps and generating a more complex response
 * than Cameras instance</i>
 * </p>
 * 
 * @author dsale
 *
 */
public class CameraLocator {

	private final CameraFilterFactory filterFactory;
	
	public CameraLocator(CameraFilterFactory filterFactory) {
		notNull(filterFactory, "Invalid parameter: filterFactory=null");
		this.filterFactory = filterFactory;
	}
	
	public CameraFilterFactory getCameraFilterFactory() {
		return filterFactory;
	}

	/**
	 * Locate the traffic cameras that are germane to the given route.
	 * @param candidates a container of candidate cameras
	 * @param route the route taken
	 * @return a container of cameras germane to the route
	 */
	public void locate(Cameras candidates, Cameras matches, Route route) {
		notNull(candidates, "Invalid parameter: cameras=null");
		notNull(matches, "Invalid parameter: matches=null");
		notNull(route, "Invalid parameter: route=null");
		
		Map<Step,CameraFilter> filters = filterFactory.getCameraFilters(route);
		
		for (Step step : route.getSteps()) {
			matches.addAll(filters.get(step).filter(candidates));
		}
	}
}