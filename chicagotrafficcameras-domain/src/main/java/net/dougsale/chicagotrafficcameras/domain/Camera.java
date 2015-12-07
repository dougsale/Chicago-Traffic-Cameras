package net.dougsale.chicagotrafficcameras.domain;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Camera implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public final Location location;
	public final Set<Approach> approaches;

	private int hashCode = 0;
	private String toString = null;
	
	/**
	 * Create a Camera instance.  Note that Camera instances are immutable.
	 * The approaches set is unmodifiable and its elements are copied
	 * from the constructor parameter set.  Location is immutable.
	 * All fields are final.
	 * 
	 * @param location coordinates per the WGS84 standard
	 * @param approaches at least one approach direction
	 */
	public Camera(Location location, Set<Approach> approaches) {
		this.location = location;

		notEmpty(approaches, "invalid approaches: null or empty");
		for (Approach approach : approaches)
			notNull(approach, "invalid approaches: null approach");
		
		// ensure the set is immutable
		this.approaches = Collections.unmodifiableSet(EnumSet.copyOf(approaches));	
	}

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
	
	public int hashCode() {
		// immutable class, calculate hashCode once, lazily
		if (hashCode  == 0)
			hashCode = new HashCodeBuilder(19, 73)
					.append(location)
					.append(approaches)
					.toHashCode();

		return hashCode;
	}
	
	public String toString() {
		// immutable class, generate toString once, lazily
		if (toString == null)
			toString = new ToStringBuilder(this)
					.append("location", location)
					.append("approaches", approaches)
					.toString();
		
		return toString;	}
}
