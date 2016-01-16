/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras;

import static org.apache.commons.lang3.Validate.notNull;

import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.repository.RepositoryException;

public class CameraService {

	private final CamerasFactory factory;
	private final CameraLocator locator;
	
	public CameraService(CamerasFactory factory, CameraLocator locator) {
		notNull(factory, "invalid parameter: factory=null");
		notNull(locator, "invalid parameter: locator=null");
		this.factory = factory;
		this.locator = locator;
	}
	
	public CamerasFactory getCamerasFactory() {
		return factory;
	}

	public CameraLocator getCameraLocator() {
		return locator;
	}
	
	public Cameras getCameras() throws RepositoryException {
		return factory.getAllCameras();
	}
	
	public Cameras getCameras(Route route) throws RepositoryException {
		notNull(route, "invalid parameter: route=null");
		return locator.locate(route);
	}
}
