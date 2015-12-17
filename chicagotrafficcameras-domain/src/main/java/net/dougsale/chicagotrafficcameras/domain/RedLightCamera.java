/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notEmpty;

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

	private static final long serialVersionUID = 1L;

	public final Set<String> intersection;

	private Integer hashCode = null;
	private String toString = null;
	
	/**
	 * Create a RedLightCamera instance.  Note that RedLightCamera instances are immutable.
	 * The intersection and approaches sets are unmodifiable and their elements are copied
	 * from the constructor parameter sets.  Location is immutable.
	 * All fields are final.
	 * 
	 * @param intersection a set of 2 or more street names
	 * @param location coordinates per the WGS84 standard
	 * @param approaches at least one approach direction
	 */
	public RedLightCamera(Set<String> intersection, Location location, Set<Approach> approaches) {
		super(location, approaches);

		// validate intersection
		isTrue(intersection.size() > 1, "invalid intersection: must contain 2 or more streets");
		for (String street : intersection)
			notEmpty(street, "invalid intersection: street null or empty");
		
		// ensure set is immutable
		this.intersection = Collections.unmodifiableSet(new HashSet<>(intersection));
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
