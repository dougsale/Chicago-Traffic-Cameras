package net.dougsale.chicagotrafficcameras.domain;

import static org.apache.commons.lang3.Validate.*;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CameraLocation {

	private static double MIN_LATITUDE = -90.0;
	private static double MAX_LATITUDE = 90.0;
	private static double MIN_LONGITUDE = -180.0;
	private static double MAX_LONGITUDE = 180.0;

	private double latitude;
	private double longitude;
	private Approach[] approaches;

	private int hashCode = 0;
	private String toString = null;
	
	public CameraLocation(double latitude, double longitude, Approach... approaches) {
		this.latitude = latitude;
		this.longitude = longitude;
		
		// validate latitude, longitude within range
		inclusiveBetween(MIN_LATITUDE, MAX_LATITUDE, latitude);
		inclusiveBetween(MIN_LONGITUDE, MAX_LONGITUDE, longitude);
		
		// validate intersection
		notEmpty(approaches, "invalid approaches: null or empty");
		for (Approach approach : approaches)
			notNull(approach, "invalid approaches: null approach");
		
		this.approaches = Arrays.copyOf(approaches, approaches.length);		
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/**
	 * @return the approaches
	 */
	public Approach[] getApproaches() {
		return Arrays.copyOf(approaches, approaches.length);
	}
	
	public boolean equals(Object object) {
		   if (object == null) return false;
		   if (object == this) return true;
		   if (object.getClass() != getClass()) return false;
		   
		   CameraLocation that = (CameraLocation) object;
		   
		   return new EqualsBuilder()
                 .append(latitude, that.latitude)
                 .append(longitude, that.longitude)
                 .append(approaches, that.approaches)
                 .isEquals();
	}
	
	public int hashCode() {
		// immutable class, calculate hashCode once, lazily
		if (hashCode  == 0)
			hashCode = new HashCodeBuilder(19, 73)
					.append(latitude).append(longitude).append(approaches)
					.toHashCode();

		return hashCode;
	}
	
	public String toString() {
		// immutable class, generate toString once, lazily
		if (toString == null)
			toString = new ToStringBuilder(this)
					.append("latitude", latitude)
					.append("longitude", longitude)
					.append("approaches", approaches)
					.toString();
		
		return toString;	}
}
