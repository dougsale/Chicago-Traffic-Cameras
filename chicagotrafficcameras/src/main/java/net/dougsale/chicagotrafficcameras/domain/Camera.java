/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Camera implements Serializable {

	private static final long serialVersionUID = 10L;
	
	// immutable class, these values are computed once, lazily
	// current values indicate that they have not been computed
	private transient Integer hashCode = null;
	private String toString = null;
	
	private final Location location;
	private final Set<Direction> approaches;

	/**
	 * Create a Camera instance.  Note that Camera instances are immutable.
	 * The approaches set is unmodifiable and its elements are copied
	 * from the constructor parameter set.  Location is immutable.
	 * All fields are final.
	 * 
	 * @param location coordinates per the WGS84 standard
	 * @param approach directions, requiring at least one entry
	 */
	public Camera(Location location, Set<Direction> approaches) {
		notNull(location);
		this.location = location;

		notNull(approaches);
		notEmpty(approaches, "invalid parameter: approaches=" + approaches);
		for (Direction approach : approaches)
			notNull(approach, "invalid parameter: approaches; contains element=null");
		
		// ensure the set is immutable
		this.approaches = Collections.unmodifiableSet(EnumSet.copyOf(approaches));	
	}

	public Location getLocation() {
		return location;
	}

	public Set<Direction> getApproaches() {
		return approaches;
	}

	@Override
	public boolean equals(Object object) {
		   if (object == null) return false;
		   if (object == this) return true;
		   if (object.getClass() != getClass()) return false;
		   
		   Camera that = (Camera) object;
		   
		   return new EqualsBuilder()
                 .append(location, that.location)
                 .append(approaches, that.approaches)
                 .isEquals();
	}
	
	@Override
	public int hashCode() {
		if (hashCode == null)
			hashCode = new HashCodeBuilder(19, 73)
				.append(location)
				.append(approaches)
				.toHashCode();
		return hashCode;
	}
	
	@Override
	public String toString() {
		if (toString == null)
			toString = new ToStringBuilder(this)
				.append("location", location)
				.append("approaches", approaches)
				.toString();
		return toString;
	}
}
