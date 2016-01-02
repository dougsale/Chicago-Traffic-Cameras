/**
 * 
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import net.dougsale.chicagotrafficcameras.domain.Camera;

/**
 * CompositeCameraFilter is a CameraFilter composed of two or
 * more CameraFilter instances.  A Camera will only be accepted
 * by this filter if it is accepted by all of its composing
 * filters.
 * 
 * @author dsale
 *
 */
public class CompositeCameraFilter extends AbstractCameraFilter {

	// Immutable class, so these are
	// computed once, lazily.
	private Integer hashCode = null;
	private String toString = null;
	
	private final CameraFilter[] filters;

	/**
	 * Creates a CompositeCameraFilter from two or more
	 * CameraFilter instances.
	 * @param filters
	 * @throws NullPointerException if filters, or any element of filters, is null
	 * @throws IllegalArgumentException if filters does not contain 2 or more CameraFilter instances
	 */
	public CompositeCameraFilter(CameraFilter... filters) {
		notNull(filters, "invalid parameter: filters=null");
		isTrue(!(filters.length < 2),
				String.format("invalid parameter: filters (length < 2); length=%d", filters.length));
		for (CameraFilter filter : filters)
			notNull(filter, "invalid parameter: filters; contains element=null");
		
		this.filters = Arrays.copyOf(filters, filters.length);
	}
	
	/**
	 * @returns an array of CameraFilter components comprising this composite
	 */
	public CameraFilter[] getCameraFilters() {
		return Arrays.copyOf(filters, filters.length);
	}

	/**
	 * Returns true if all CameraFilter components of this composite accept camera
	 * @param camera
	 * @throws NullPointerException if camera is null
	 */
	@Override
	public boolean accept(Camera camera) {
		notNull(camera, "invalid parameter: camera=null");
		
		boolean accept = true;
		
		for (CameraFilter filter : filters) {
			if (!filter.accept(camera)) {
				accept = false;
				break;
			}
		}
		
		return accept;
	}

	
	@Override
	public boolean equals(Object object) {
	   if (object == null) return false;
	   if (object == this) return true;
	   if (object.getClass() != getClass()) return false;
	   
	   CompositeCameraFilter that = (CompositeCameraFilter) object;
	   
	   return new EqualsBuilder()
	                 .append(filters, that.filters)
	                 .isEquals();
	}

	@Override
	public int hashCode() {
		// immutable; compute hashCode once, lazily
		if (hashCode == null)
			hashCode = new HashCodeBuilder(19, 73)
					.append(filters)
					.toHashCode();
		return hashCode;
	}

	@Override
	public String toString() {
		// immutable; generate toString once, lazily
		if (toString == null)
			toString = new ToStringBuilder(this)
				.append("filters", filters)
				.toString();

		return toString;
	}
}
