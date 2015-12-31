/**
 * 
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.apache.commons.lang3.Validate.notNull;

import net.dougsale.chicagotrafficcameras.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Camera;

/**
 * @author dsale
 *
 */
public abstract class AbstractCameraFilter implements CameraFilter {

	/**
	 * Filters the cameras, returning those
	 * that do match a subclass' accept implementation.
	 * Modifies the provided cameras container,
	 * returning the same instance.
	 * @param cameras candidate cameras
	 * @returns cameras that are accepted by subclass' accept implementation
	 * @throws NullPointerException if cameras is null
	 */
	@Override
	public Cameras filter(Cameras cameras) {
		notNull(cameras, "invalid parameter: cameras=null");
		
		for (Camera camera : cameras.get())
			if (!accept(camera))
				cameras.remove(camera);

		return cameras;
	}
}
