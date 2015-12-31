/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import net.dougsale.chicagotrafficcameras.domain.Camera;

/**
 * Cameras is a container for Camera instances.
 * @author dsale
 */
public class Cameras implements Serializable {
	
	private static final long serialVersionUID = 3L;

	public static final Comparator<Camera> BY_LATITUDE = new Comparator<Camera>() {
		@Override
		public int compare(Camera c1, Camera c2) {
			return 
					c1.getLocation().latitude < c2.getLocation().latitude? -1 :
					c1.getLocation().latitude > c2.getLocation().latitude? 1 : 0;
		}
	};
			
	public static final Comparator<Camera> BY_LONGITUDE = new Comparator<Camera>() {
		@Override
		public int compare(Camera c1, Camera c2) {
			return
					c1.getLocation().longitude < c2.getLocation().longitude? -1 :
					c1.getLocation().longitude > c2.getLocation().longitude? 1 : 0;
		}			
	};
	
	private HashMap<Class<? extends Camera>, Set<? extends Camera>> camerasByType = new HashMap<>();

	/**
	 * Create Cameras instance.
	 */
	public Cameras() {}
	
	/**
	 * Add a Camera to this Cameras container.
	 * @param camera
	 * @throws NullPointerException if camera is null
	 */
	@SuppressWarnings("unchecked")
	public <T extends Camera> void add(T camera) {
		notNull(camera, "invalid parameter: camera=null");
		
		Class<? extends Camera> type = camera.getClass();
		
		if (!camerasByType.containsKey(type))
			camerasByType.put(type, new HashSet<T>());

		((Set<T>)camerasByType.get(type)).add(camera);
	}
	
	public void addAll(Cameras cameras) {
		throw new UnsupportedOperationException("Oh!  You needed this?");
	}
		
	public void remove(Camera camera) {
		throw new UnsupportedOperationException("Oh!  You needed this?");
	}
	
	public int size() {
		int size = 0;
		for (Set<? extends Camera> set : camerasByType.values())
			size += set.size();
		return size;
	}
	
	/**
	 * Returns the Set of Camera types in this Cameras container.
	 * @return the Set of Camera types
	 */
	public Set<Class<? extends Camera>> getTypes() {
		Set<Class<? extends Camera>> types = new HashSet<>();
		types.addAll(camerasByType.keySet());
		return types;
	}

	/**
	 * Returns the Set of all Camera instances in this Cameras container.
	 * @return the Set of all Camera instances
	 */
	public Set<Camera> get() {
		Set<Camera> cameras = new HashSet<>();
		
		for (Class<? extends Camera> type : camerasByType.keySet())
			cameras.addAll(camerasByType.get(type));
		
		return cameras;
	}
	
	/**
	 * Returns the NavigableSet of all Camera instances in this Cameras container. 
	 * @param comparator used to order the set
	 * @return the NavigableSet of all Camera instances
	 * @throws NullPointerException if comparator is null
	 */
	public NavigableSet<Camera> get(Comparator<? super Camera> comparator) {
		notNull(comparator, "invalid parameter: comparator=null");
		
		NavigableSet<Camera> cameras = new TreeSet<>(comparator);
		
		for (Class<? extends Camera> type : camerasByType.keySet())
			cameras.addAll(camerasByType.get(type));
		
		return cameras;
	}
	
	/**
	 * Returns the Set of all Camera instances of type in this Cameras container.
	 * @param type a camera type
	 * @return the Set of all Camera instances of type
	 * @throws NullPointerException if type is null
	 */
	@SuppressWarnings("unchecked")
	public <T extends Camera> Set<T> get(Class<T> type) {
		notNull(type, "invalid parameter: type=null");
		
		Set<T> cameras = new HashSet<>();

		if (camerasByType.containsKey(type))
			cameras.addAll((Set<T>)camerasByType.get(type));

		return cameras;
	}
	
	/**
	 * Returns the NavigableSet of all Camera instances of type in this Cameras container. 
	 * @param type a camera type
	 * @param comparator used to order the set
	 * @return the NavigableSet of all Camera instances of type
	 * @throws NullPointerException if type is null or comparator is null
	 */
	@SuppressWarnings("unchecked")
	public <T extends Camera> NavigableSet<T> get(Class<T> type, Comparator<? super T> comparator) {
		notNull(type, "invalid parameter: type=null");
		notNull(comparator, "invalid parameter: comparator=null");
		
		NavigableSet<T> cameras = new TreeSet<>(comparator);
		
		if (camerasByType.containsKey(type))
			cameras.addAll((Set<T>)camerasByType.get(type));

		return cameras;
	}

	@Override
	public boolean equals(Object object) {
		   if (object == null) return false;
		   if (object == this) return true;
		   if (object.getClass() != getClass()) return false;
		   
		   Cameras that = (Cameras) object;
		   
		   return new EqualsBuilder()
                 .append(camerasByType, that.camerasByType)
                 .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(19, 73)
					.append(camerasByType)
					.toHashCode();
	}


}