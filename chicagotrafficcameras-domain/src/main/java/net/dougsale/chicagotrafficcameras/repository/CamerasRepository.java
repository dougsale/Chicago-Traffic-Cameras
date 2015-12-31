/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.repository;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.io.InputStream;
import java.io.InvalidClassException;

import net.dougsale.chicagotrafficcameras.Cameras;

public class CamerasRepository {
	
	private final String resourceName;
	private Cameras cameras = null;
	
	public CamerasRepository(String resourceName) {
		notNull(resourceName, "invalid parameter: resourceName=null");
		notEmpty(resourceName.trim(), String.format("invalid parameter: resourceName=\"%s\"", resourceName));
		this.resourceName = resourceName; 
	}
	
	public void init() throws RepositoryException {
		try (InputStream inputStream = CamerasRepository.class.getClassLoader().getResourceAsStream(resourceName)) {
			if (inputStream == null)
				throw new RepositoryException(String.format("Repository resource not available: \"%s\"", resourceName));

			try (ObjectInputStream stream = new ObjectInputStream(inputStream)) {
				cameras = (Cameras) stream.readObject();
			} catch (ClassNotFoundException | InvalidClassException | StreamCorruptedException | OptionalDataException e) {
				throw new RepositoryException("Repository resource is possibly not synchronized with changes to the classes of the objects it contains", e);
			}
		} catch (IOException e) {
			throw new RepositoryException("Repository I/O error", e);
		}
	}
	
	public String getResourceName() {
		return resourceName;
	}
	
	public Cameras getCameras() throws RepositoryException {
		if (cameras == null) {
			init();
		}
		return cameras;
	}
}
