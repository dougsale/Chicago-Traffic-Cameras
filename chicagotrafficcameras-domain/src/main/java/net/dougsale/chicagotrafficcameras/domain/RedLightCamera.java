/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author dsale
 *
 */
public class RedLightCamera extends Camera {

	private static final long serialVersionUID = 3L;

	// immutable class, these values are computed once, lazily
	// current values indicate that they have not been computed
	private Integer hashCode = null;
	private String toString = null;
	
	private final Set<String> intersection;

	/**
	 * Create a RedLightCamera instance.  Note that RedLightCamera instances are immutable.
	 * The intersection and approaches sets are unmodifiable and their elements are copied
	 * from the constructor parameter sets.  Location is immutable.
	 * All fields are final.
	 * 
	 * @param intersection street names identifying the intersection (at least 2)
	 * @param location camera location
	 * @param approaches camera approach direction (at least one)
	 */
	public RedLightCamera(Set<String> intersection, Location location, Set<Direction> approaches) {
		super(location, approaches);

		// validate intersection
		isTrue(!(intersection.size() < 2),
				String.format("invalid parameter: intersection (size < 2); size=%d", intersection.size()));
		for (String street : intersection) {
			notNull(street, "invalid parameter: intersection; contains element=null");
			notEmpty(street.trim(), String.format("invalid parameter: street=<%s>", street));
		}
		
		// ensure intersection is immutable
		this.intersection = Collections.unmodifiableSet(new HashSet<>(intersection));
	}

	public Set<String> getIntersection() {
		return intersection;
	}

	@Override
	public boolean equals(Object object) {
	   if (object == null) return false;
	   if (object == this) return true;
	   if (object.getClass() != getClass()) return false;
	   
	   RedLightCamera that = (RedLightCamera) object;
	   
	   return new EqualsBuilder()
	                 .appendSuper(super.equals(object))
	                 .append(intersection, that.intersection)
	                 .isEquals();
	}

	@Override
	public int hashCode() {
		// immutable class, calculate hashCode once, lazily
		if (hashCode == null)
			hashCode = new HashCodeBuilder(19, 73)
					.appendSuper(super.hashCode())
					.append(intersection)
					.toHashCode();

		return hashCode;
	}

	@Override
	public String toString() {
		// immutable class, generate toString once, lazily
		if (toString == null)
			toString = new ToStringBuilder(this)
					.appendSuper(super.toString())
					.append("intersection", intersection)
					.toString();
		
		return toString;
	}
}
