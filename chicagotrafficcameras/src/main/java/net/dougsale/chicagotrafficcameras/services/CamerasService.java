/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.services;

import static org.apache.commons.lang3.Validate.notNull;

import net.dougsale.chicagotrafficcameras.domain.CameraLocator;
import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.CamerasFactory;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.repository.RepositoryException;

public class CamerasService {

	private final CamerasFactory factory;
	private final CameraLocator locator;
	
	public CamerasService(CamerasFactory factory, CameraLocator locator) {
		notNull(factory);
		notNull(locator);
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
		notNull(route);
		return locator.locate(route);
	}
}
