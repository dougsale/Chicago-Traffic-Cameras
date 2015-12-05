/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A container for cameras, allowing retrieval of cameras by type.
 * @author dsale
 *
 */
public class Cameras {
	
	private Map<Class<? extends Camera>, Set<? extends Camera>> camerasByType = new HashMap<>();

	/**
	 * Create Cameras instance.
	 */
	public Cameras() {}
	
	/**
	 * Add a camera to this Cameras container.
	 * @param camera
	 */
	public <T extends Camera> void add(T camera) {
		notNull(camera, "camera must be non-null");
		
		@SuppressWarnings("unchecked")
		Set<T> cameras = (Set<T>) camerasByType.get(camera.getClass());

		if (cameras == null) {
			cameras = new HashSet<T>();
			camerasByType.put(camera.getClass(), cameras);
		}
		
		cameras.add(camera);	
	}

	/**
	 * Retrieves all cameras of the given type.
	 * @param type a camera subclass
	 * @return
	 */
	public  <T extends Camera> Set<T> get(Class<T> type) {
		@SuppressWarnings("unchecked")
		Set<T> cameras = (Set<T>) camerasByType.get(type);
		return cameras == null? Collections.emptySet() : Collections.unmodifiableSet(cameras);
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<Class<? extends Camera>> getTypes() {
		return Collections.unmodifiableSet(camerasByType.keySet());
	}
}
