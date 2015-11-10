/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain;

import static org.apache.commons.lang3.Validate.notEmpty;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author dsale
 *
 */
public class RedLightCameraLocation extends CameraLocation {

	private String[] intersection;

	private int hashCode = 0;
	private String toString = null;
	
	/**
	 * 
	 */
	public RedLightCameraLocation(String[] intersection, double latitude, double longitude, Approach[] approaches) {
		super(latitude, longitude, approaches);

		// validate intersection
		notEmpty(intersection, "invalid intersection: null or empty");
		for (String street : intersection)
			notEmpty(street, "invalid intersection: null or empty street");
		
		this.intersection = Arrays.copyOf(intersection, intersection.length);
	}

	/**
	 * @return the intersection
	 */
	public String[] getIntersection() {
		return Arrays.copyOf(intersection, intersection.length);
	}

	public boolean equals(Object object) {
	   if (object == null) return false;
	   if (object == this) return true;
	   if (object.getClass() != getClass()) return false;
	   
	   RedLightCameraLocation that = (RedLightCameraLocation) object;
	   
	   return new EqualsBuilder()
	                 .appendSuper(super.equals(object))
	                 .append(intersection, that.intersection)
	                 .isEquals();
	}

	public int hashCode() {
		// immutable class, calculate hashCode once, lazily
		if (hashCode == 0)
			hashCode = new HashCodeBuilder(19, 73)
					.appendSuper(super.hashCode()).append(intersection)
					.toHashCode();

		return hashCode;
	}

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
