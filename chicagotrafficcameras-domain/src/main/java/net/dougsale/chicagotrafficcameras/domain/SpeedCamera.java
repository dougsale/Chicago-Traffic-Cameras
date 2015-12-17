/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain;

import static org.apache.commons.lang3.Validate.notEmpty;

import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author dsale
 *
 */
public class SpeedCamera extends Camera {

	private static final long serialVersionUID = 1L;

	public final String address;

	// immutable class, these values are computed once, lazily
	// current values indicate that they have not been computed
	private Integer hashCode = null;
	private String toString = null;
	
	/**
	 * Create a SpeedCamera instance.  Note that SpeedCamera instances are immutable.
	 * The approaches set is unmodifiable and its elements are copied
	 * from the constructor parameter sets.  Location is immutable.
	 * All fields are final.
	 * 
	 * @param address the street address of the camera
	 * @param location coordinates per the WGS84 standard
	 * @param approaches at least one approach direction
	 */
	public SpeedCamera(String address, Location location, Set<Approach> approaches) {		
		super(location, approaches);
		
		notEmpty(address, "invalid address: null or empty");
		this.address = address;
	}

	public boolean equals(Object o) {
	   if (o == null) return false;
	   if (o == this) return true;
	   if (o.getClass() != getClass()) return false;
	   
	   SpeedCamera that = (SpeedCamera) o;
	   
	   return new EqualsBuilder()
	                 .appendSuper(super.equals(o))
	                 .append(address, that.address)
	                 .isEquals();
	}

	public int hashCode() {
		// immutable class, calculate hashCode once, lazily
		if (hashCode == null)
			hashCode = new HashCodeBuilder(19, 73)
					.appendSuper(super.hashCode())
					.append(address)
					.toHashCode();

		return hashCode;
	}

	public String toString() {
		// immutable class, generate toString once, lazily
		if (toString == null)
			toString = new ToStringBuilder(this)
					.appendSuper(super.toString())
					.append("address", address)
					.toString();
		
		return toString;
	}
}