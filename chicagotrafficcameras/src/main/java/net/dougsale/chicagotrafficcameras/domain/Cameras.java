/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain;

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
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Cameras is a container for Camera instances.
 * @author dsale
 */
public class Cameras implements Serializable {
	
	private static final long serialVersionUID = 10L;

	//TODO review code for efficiency, clarity wrt: new/copy/clone/empty, generics, return types, et al
	
	/**
	 * A comparator that sorts Camera instances by their location's latitude.
	 */
	public static final Comparator<Camera> BY_LATITUDE = new Comparator<Camera>() {
		@Override
		public int compare(Camera c1, Camera c2) {
			return 
					c1.getLocation().latitude < c2.getLocation().latitude? -1 :
					c1.getLocation().latitude > c2.getLocation().latitude? 1 : 0;
		}
	};
			
	/**
	 * A comparator that sorts Camera instances by their location's longitude.
	 */
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
	 * Add the Camera instance to this Cameras collection.
	 * @param camera
	 * @throws NullPointerException if camera is null
	 */
	@SuppressWarnings("unchecked")
	public <T extends Camera> boolean add(T camera) {
		notNull(camera, "invalid parameter: camera=null");
		
		Class<T> type = (Class<T>) camera.getClass();		
		Set<T> set = (Set<T>) camerasByType.get(type);

		if (set == null) {
			set = new HashSet<T>();
			camerasByType.put(type, set);
		}
		
		return set.add(camera);
	}
	
	/**
	 * Add all Camera instances in cameras to this Cameras collection.
	 * @param cameras
	 * @throws NullPointerException if cameras is null
	 */
	public void addAll(Cameras cameras) {
		notNull(cameras, "invalid parameter: cameras=null");
		//TODO non-naive implementation that correctly handles generics
		for (Camera camera : cameras.get())
			add(camera);
	}
	
	/**
	 * Remove the Camera instance from this Cameras collection. 
	 * @param camera
	 * @throws NullPointerException if camera is null
	 */
	@SuppressWarnings("unchecked")
	public <T extends Camera> boolean remove(T camera) {
		notNull(camera, "invalid parameter: camera=null");
		Set<T> cameras = (Set<T>) camerasByType.get(camera.getClass());
		return cameras == null? false : cameras.remove(camera);
	}
	
	/**
	 * Returns the count of all Camera instances contained.
	 * @return count of Camera instances
	 */
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

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(camerasByType)
				.toString();
	}
}