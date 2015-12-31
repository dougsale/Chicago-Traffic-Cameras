/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.Location;

/**
 * A CameraFilter implementation that utilizes a
 * bounding box to filter Cameras by location.
 * 
 * @author dsale
 */
public class BoundingBox extends AbstractCameraFilter {

	// Immutable class, so these are
	// computed once, lazily.
	private Integer hashCode = null;
	private String toString = null;
	
	public final Location min;
	public final Location max;
	
	/**
	 * @param min a location representing the corner of the box with smallest latitude and longitude
	 * @param max a location representing the corner of the box with largest latitude and longitude
	 * @throws NullPointerException if either min or max are null
	 * @throws IllegalArgumentException if either min.latitude > max.latitude or min.longitude > max.longitude
	 */
	public BoundingBox(Location min, Location max) {
		notNull(min, "invalid parameter: min = null");
		notNull(max, "invalid parameter: max = null");
		isTrue(min.latitude <= max.latitude,
				"invalid parameters: min.latitude=" + min.latitude + " max.latitude=" + max.latitude);
		isTrue(min.longitude <= max.longitude,
				"invalid parameters: min.longitude=" + min.longitude + " max.longitude=" + max.longitude);
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Returns true if the camera lies within
	 * the bounding box.
	 * @param camera
	 * @returns true if the camera lies within the bounding box
	 * @throws NullPointerException if camera is null
	 */
	@Override
	public boolean accept(Camera camera) {
		notNull(camera, "invalid parameter: camera=null");
		
		Location location = camera.getLocation();
		
		boolean inBounds =
				location.latitude >= min.latitude && location.latitude <= max.latitude && 
				location.longitude >= min.longitude && location.longitude <= max.longitude;
				
		return inBounds;
	}
	
	//SOMETHING IS WRONG WITH THIS OPTIMIZATION - it has to do with the comparator and set semantics, methinks
//	Camera lower = new Camera(min, EnumSet.of(Approach.EASTBOUND));
//	Camera upper = new Camera(max, EnumSet.of(Approach.EASTBOUND));
//	
//	for (Camera camera : cameras.get(getCameraComparator()).subSet(lower, true, upper, true)) {
//		System.out.println(camera);
//		if (accept(camera))
//			filtered.add(camera);
//	}
//because this doesn't work either:
//	Set<Camera> cs = cameras.get(getCameraComparator());
//	for (Camera c : cs)
//		if (accept(c))
//			filtered.add(c);
	// At 41.850 degrees latitude (~Chicago),
	// 	 1 degree of longitude is roughly 45 nautical miles
	//   1 degree of latitude is roughly 60 nautical miles
	// So, a rough estimate for comparing degrees longitude to
	// latitude at Chicago's latitude is:
//	static final double LONG_OVER_LAT = 0.75;
//
//	//
//	Comparator<Camera> getCameraComparator() {
//		return ((max.latitude - min.latitude) * LONG_OVER_LAT < (max.longitude - min.longitude))?
//				Cameras.BY_LATITUDE : Cameras.BY_LONGITUDE;
//	}

	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (object == this) return true;
		if (object.getClass() != getClass()) return false;

		BoundingBox that = (BoundingBox) object;

		return new EqualsBuilder()
			.append(min, that.min)
			.append(max, that.max)
			.isEquals();
	}

	@Override
	public int hashCode() {
		// immutable; compute hashCode once, lazily
		if (hashCode == null)
			hashCode = new HashCodeBuilder(19, 73)
				.append(min)
				.append(max)
				.toHashCode();

		return hashCode;
	}

	@Override
	public String toString() {
		// immutable; generate toString once, lazily
		if (toString == null)
			toString = new ToStringBuilder(this)
				.append("min", min)
				.append("max", max)
				.toString();

		return toString;
	}

}