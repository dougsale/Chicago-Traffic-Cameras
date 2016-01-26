/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain;

import static org.apache.commons.lang3.Validate.notNull;

import net.dougsale.chicagotrafficcameras.repository.CamerasRepository;
import net.dougsale.chicagotrafficcameras.repository.RepositoryException;

/**
 * @author dsale
 *
 */
public class CamerasFactory {
	
	private final CamerasRepository repository;

	private Cameras allCameras = null;
	
	public CamerasFactory(CamerasRepository repository) {
		notNull(repository);
		this.repository = repository;
	}

	public CamerasRepository getRepository() {
		return repository;
	}

	public Cameras getEmptyCameras() {
		return new Cameras();
	}

	public Cameras getAllCameras() throws RepositoryException {
		if (allCameras == null)
			allCameras = repository.getCameras();
		
		Cameras cameras = new Cameras();
		cameras.addAll(allCameras);
		
		return cameras;
	}
}
