/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras;

import java.io.IOException;

import net.dougsale.chicagotrafficcameras.repository.CamerasRepository;

public class CameraLocatorService {

	private CameraLocator locator;
	private Cameras allCameras;
	
	public CameraLocatorService() {
	}
	
	public void init() throws ClassNotFoundException, IOException {
		allCameras = new CamerasRepository("Cameras.ser").getCameras();
		locator = new CameraLocator(allCameras);
	}
	
	public Cameras locateCameras() {
		return allCameras;
	}
	
	public Cameras locateCameras(Directions directions) {
		return locator.locate(directions);
	}
}
