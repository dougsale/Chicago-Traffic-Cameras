package net.dougsale.chicagotrafficcameras.repository;

import java.io.IOException;
import java.io.ObjectInputStream;

import net.dougsale.chicagotrafficcameras.Cameras;

public class CamerasRepository {
	
	private String camerasResource;
	
	public CamerasRepository(String camerasResource) {
		this.camerasResource = camerasResource; 
	}
	
	public Cameras getCameras() throws IOException, ClassNotFoundException {
		Cameras cameras = null;
		
		try (ObjectInputStream stream = new ObjectInputStream(
				CamerasRepository.class.getClassLoader().getResourceAsStream(camerasResource))) {
			
			cameras = (Cameras) stream.readObject();
			
		}
		
		return cameras;
	}
}
