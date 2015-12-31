/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras;

import static org.apache.commons.lang3.Validate.notNull;

import net.dougsale.chicagotrafficcameras.repository.CamerasRepository;
import net.dougsale.chicagotrafficcameras.repository.RepositoryException;

public class CameraService {

	private final CameraLocator locator;
	private final CamerasRepository repository;
	
	public CameraService(CamerasRepository repository, CameraLocator locator) {
		notNull(repository, "invalid parameter: repository=null");
		notNull(locator, "invalid parameter: locator=null");
		this.repository = repository;
		this.locator = locator;
	}
	
	public CamerasRepository getCamerasRepository() {
		return repository;
	}

	public CameraLocator getCameraLocator() {
		return locator;
	}
	
	public Cameras getCameras() throws RepositoryException {
		return repository.getCameras();
	}
	
	public Cameras getCameras(Route route) throws RepositoryException {
		notNull(route, "invalid parameter: route=null");
		Cameras cameras = new Cameras();
		locator.locate(repository.getCameras(), cameras, route);
		return cameras;
	}
}
