/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.repository;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import net.dougsale.chicagotrafficcameras.domain.Cameras;

public class CamerasRepository {
	
	private final String resourceName;
	
	/**
	 * Creates a CamerasRepository that reads from a serialized {@link Cameras} instance
	 * resource identified by resourceName.  The resource must be available via
	 * the same {@link ClassLoader} that loaded this class.
	 * @param resourceName identifies a serialized {@link Cameras} instance
	 * @throws {@link NullPointerException} if resourceName is null
	 * @throws {@link IllegalArgumentException} if resourceName is empty
	 */
	public CamerasRepository(String resourceName) {
		notNull(resourceName);
		notEmpty(resourceName.trim(), String.format("invalid parameter: resourceName=\"%s\"", resourceName));
		
		this.resourceName = resourceName; 
	}
	
	/**
	 * Returns the identifier used to locate the serialized {@link Cameras} instance.
	 * @returns the resource name
	 */
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * Returns the {@link Cameras} instance identified by resource name by requesting
	 * a stream from this class' {@link ClassLoader} and deserializing the bytes read
	 * from it.
	 * @returns the {@link Cameras} instance identified by resource name
	 * @throws RepositoryException if the resource could not be resolved to a stream,
	 *   the resource is not compatible with current class versions or
	 *   has been corrupted, or a general i/o error occurs.
	 */
	public Cameras getCameras() throws RepositoryException {
		ObjectInputStream in = getObjectInputStream(getResourceAsStream());
		Cameras cameras = readCameras(in);
		close(in);
		return cameras;
	}

	// breaking out these helper methods allows for testing of exception handling functionality
	// otherwise, the whole mess could be covered by classnotfoundexception and ioexception

	//
	// this method takes ObjectInput instead of ObjectInputStream, for testing purposes:
	//   can't mock ObjectInputStream.readObject() with Mockito, as it is final
	//
	// it does differentiate between a generic io exception (per ObjectInput), and those specific to
	//   the ObjectInputStream implementation; it's useful to know the difference between
	//   an invalid serialized resource and someone tripping over a network cable
	//
	Cameras readCameras(ObjectInput in) throws RepositoryException {
		try {
			return (Cameras) in.readObject();
		} catch (ClassNotFoundException e) {
			throw new RepositoryException(RepositoryErrorCodes.MISSING_CLASS, e).withContext("resourceName", resourceName);			
		} catch (InvalidClassException | StreamCorruptedException | OptionalDataException e) {
			throw new RepositoryException(RepositoryErrorCodes.INVALID_FORMAT, e).withContext("resourceName", resourceName);
		} catch (IOException e) {
			throw new RepositoryException(RepositoryErrorCodes.READ_FAILED, e).withContext("resourceName", resourceName);
		}
	}

	InputStream getResourceAsStream() throws RepositoryException {
		InputStream inputStream = CamerasRepository.class.getClassLoader().getResourceAsStream(resourceName);
		if (inputStream == null)
			throw new RepositoryException(RepositoryErrorCodes.UNAVAILABLE).withContext("resourceName", resourceName);
		return inputStream;
	}
	
	// objectinputstream reads some header info on instantiation, this method's
	// error handling differentiates between an invalid repo resource and a generic io error
	ObjectInputStream getObjectInputStream(InputStream in) throws RepositoryException {
		try {
			return new ObjectInputStream(in);
		} catch (StreamCorruptedException e) {
			throw new RepositoryException(RepositoryErrorCodes.INVALID_FORMAT, e).withContext("resourceName", resourceName);
		} catch (IOException e) {
			throw new RepositoryException(RepositoryErrorCodes.READ_FAILED, e).withContext("resourceName", resourceName);
		}
	}
	
	void close(InputStream in) throws RepositoryException {
		try { in.close(); } catch (IOException e) { 
			// error closing stream... maybe should just log warning and continue execution (i.e., eat exception)
			throw new RepositoryException(RepositoryErrorCodes.CLOSE_FAILED, e).withContext("resourceName", resourceName);
		}
	}
}
