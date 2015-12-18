package net.dougsale.chicagotrafficcameras.repository;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.io.InputStream;

import net.dougsale.chicagotrafficcameras.Cameras;

public class CamerasRepository {
	
	private String camerasResource;
	
	public CamerasRepository(String camerasResource) {
		notNull(camerasResource, "invalid parameter: camerasResource=" + camerasResource);
		notEmpty(camerasResource.trim(), "invalid parameter: camerasResource=" + camerasResource);
		this.camerasResource = camerasResource; 
	}
	
	public Cameras getCameras() throws IOException, ClassNotFoundException {
		Cameras cameras = null;
		
		try (InputStream inputStream = CamerasRepository.class.getClassLoader().getResourceAsStream(camerasResource)) {
			if (inputStream == null)
				throw new IOException("Resource not available: " + camerasResource);

			try (ObjectInputStream stream = new ObjectInputStream(inputStream)) {
				cameras = (Cameras) stream.readObject();
			}
		}
		
		return cameras;
	}
}
