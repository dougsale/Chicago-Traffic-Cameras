package net.dougsale.chicagotrafficcameras.domain;

import static org.apache.commons.lang3.Validate.inclusiveBetween;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Location implements Serializable {

	private static final long serialVersionUID = 1L;
		
	// these are the bounds set by WGS84 encoding,
	// the scheme used by google maps
	private static double MIN_LATITUDE = -90.0;
	private static double MAX_LATITUDE = 90.0;
	private static double MIN_LONGITUDE = -180.0;
	private static double MAX_LONGITUDE = 180.0;

	// immutable class, these values are computed once, lazily
	// current values indicate that they have not been computed
	private Integer hashCode = null;
	private String toString = null;
	
	public final double latitude;
	public final double longitude;

	public Location(double latitude, double longitude) {
		// validate latitude, longitude within range
		inclusiveBetween(MIN_LATITUDE, MAX_LATITUDE, latitude, "invalid parameter: latitude=" + latitude + " (out of range)");
		inclusiveBetween(MIN_LONGITUDE, MAX_LONGITUDE, longitude, "invalid parameter: longitude=" + longitude + " (out of range)");

		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (object == this) return true;
		if (object.getClass() != getClass()) return false;

		Location that = (Location) object;

		return new EqualsBuilder()
			.append(latitude, that.latitude)
			.append(longitude, that.longitude)
			.isEquals();
	}

	@Override
	public int hashCode() {
		// immutable class, calculate hashCode once, lazily
		if (hashCode == null)
			hashCode = new HashCodeBuilder(19, 73)
				.append(latitude)
				.append(longitude)
				.toHashCode();

		return hashCode;
	}

	@Override
	public String toString() {
		// immutable class, generate toString once, lazily
		if (toString == null)
			toString = new ToStringBuilder(this)
				.append("latitude", latitude)
				.append("longitude", longitude)
				.toString();

		return toString;
	}
}