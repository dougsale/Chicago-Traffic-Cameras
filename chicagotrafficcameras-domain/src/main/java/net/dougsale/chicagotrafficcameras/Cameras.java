/**
 * 
 */
package net.dougsale.chicagotrafficcameras;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import net.dougsale.chicagotrafficcameras.domain.Camera;

/**
 * A container for cameras, allowing retrieval of cameras by type, with or without imposed ordering
 * @author dsale
 */
public class Cameras implements Serializable {
	
	public enum Sorted {
		BY_LATITUDE(new Comparator<Camera>() {
			@Override
			public int compare(Camera c1, Camera c2) {
				return (c1.location.latitude < c2.location.latitude)? -1 : (c1.location.latitude > c2.location.latitude)? 1 : 0;
			}			
		}),
		BY_LONGITUDE(new Comparator<Camera>() {
			@Override
			public int compare(Camera c1, Camera c2) {
				return (c1.location.longitude < c2.location.longitude)? -1 : (c1.location.longitude > c2.location.longitude)? 1 : 0;
			}			
		}),
		BY_LATITUDE_DECREASING(new Comparator<Camera>() {
			@Override
			public int compare(Camera c1, Camera c2) {
				return (c1.location.latitude < c2.location.latitude)? 1 : (c1.location.latitude > c2.location.latitude)? -1 : 0;
			}			
		}),
		BY_LONGITUDE_DECREASING(new Comparator<Camera>() {
			@Override
			public int compare(Camera c1, Camera c2) {
				return (c1.location.longitude < c2.location.longitude)? 1 : (c1.location.longitude > c2.location.longitude)? -1 : 0;
			}			
		});
		
		final Comparator<Camera> comparator;
		
		Sorted(Comparator<Camera> comparator) {
			this.comparator = comparator;
		}
	}
	
	private static final long serialVersionUID = 1L;

	private HashMap<Class<? extends Camera>, Set<? extends Camera>> camerasByType = new HashMap<>();

	/**
	 * Create Cameras instance.
	 */
	public Cameras() {}
	
	/**
	 * Add a camera to this Cameras container.
	 * @param camera
	 */
	public <T extends Camera> void add(T camera) {
		notNull(camera, "invalid parameter: camera=" + camera);
		
		@SuppressWarnings("unchecked")
		Set<T> cameras = (Set<T>) camerasByType.get(camera.getClass());

		if (cameras == null) {
			cameras = new HashSet<T>();
			camerasByType.put(camera.getClass(), cameras);
		}
		
		cameras.add(camera);	
	}
	
	/**
	 * Returns the set of camera types contained in this Cameras instance.
	 * @return the set of camera types
	 */
	public Set<Class<? extends Camera>> getTypes() {
		return Collections.unmodifiableSet(camerasByType.keySet());
	}

	/**
	 * Retrieves a Set of all cameras of the given type contained in this Cameras instance.
	 * @param type a camera subclass
	 * @return a set of cameras of the given type
	 */
	public  <T extends Camera> Set<T> get(Class<T> type) {
		notNull(type, "invalid parameter: type=" + type);
		
		@SuppressWarnings("unchecked")
		Set<T> cameras = (Set<T>) camerasByType.get(type);
		return cameras == null? Collections.emptySet() : Collections.unmodifiableSet(cameras);
	}
	
	/**
	 * Retrieves a NavigableSet of all cameras of the given type contained in this Cameras instance.
	 * @param type a camera subclass
	 * @param sorted the ordering criteria for the given set
	 * @return a set of cameras of the given type
	 */
	public <T extends Camera> NavigableSet<T> get(Class<T> type, Sorted sorted) {
		notNull(type, "invalid parameter: type=" + type);
		notNull(sorted, "invalid parameter: sorted=" + sorted);
		
		@SuppressWarnings("unchecked")
		Set<T> cameras = (Set<T>) camerasByType.get(type);
		if (cameras == null) {
			return new TreeSet<T>();
		}
		else {
			TreeSet<T> sortedCameras = new TreeSet<>(sorted.comparator);
			sortedCameras.addAll(cameras);
			return sortedCameras;
		}
	}	
}
