/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import net.dougsale.chicagotrafficcameras.camerafilters.CameraFilter;
import net.dougsale.chicagotrafficcameras.camerafilters.CameraFilterFactory;
import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.Route.Step;
import net.dougsale.chicagotrafficcameras.repository.RepositoryException;

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

	private final CamerasFactory camerasFactory;
	private final CameraFilterFactory filterFactory;
	
	public CameraLocator(CamerasFactory camerasFactory, CameraFilterFactory filterFactory) {
		notNull(camerasFactory, "Invalid parameter: camerasFactory=null");
		notNull(filterFactory, "Invalid parameter: filterFactory=null");
		this.camerasFactory = camerasFactory;
		this.filterFactory = filterFactory;
	}
	
	public CamerasFactory getCamerasFactory() {
		return camerasFactory;
	}

	public CameraFilterFactory getCameraFilterFactory() {
		return filterFactory;
	}

	/**
	 * Locate the traffic cameras that are germane to the given route.
	 * @param candidates a container of candidate cameras
	 * @param route the route taken
	 * @throws RepositoryException if retrieving cameras fails
	 * @returns a container of cameras germane to the route
	 */
	public Cameras locate(Route route) throws RepositoryException {
		notNull(route, "Invalid parameter: route=null");
		
		Map<Step,CameraFilter> filters = filterFactory.getCameraFilters(route);
		Cameras candidates = camerasFactory.getAllCameras();
		Cameras accepted = camerasFactory.getEmptyCameras();
		
		route.getSteps().stream()
			.map(step -> { return filters.get(step); })
			.forEach(filter -> {
				candidates.get().stream()
					.filter(camera -> { return filter.accept(camera); })
					.forEach(camera -> { accepted.add(camera); });			
		});

		return accepted;
	}
}