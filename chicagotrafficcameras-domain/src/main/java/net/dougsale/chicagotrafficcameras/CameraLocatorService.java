package net.dougsale.chicagotrafficcameras;

import java.io.IOException;

import net.dougsale.chicagotrafficcameras.repository.CamerasRepository;

public class CameraLocatorService {

	private CameraLocator locator;
	
	public CameraLocatorService() {
	}
	
	public void init() throws ClassNotFoundException, IOException {
		locator = new CameraLocator(new CamerasRepository("Cameras.ser").getCameras());
	}
	
	public Cameras locateCameras(Directions directions) {
		return locator.locate(directions);
	}
}
