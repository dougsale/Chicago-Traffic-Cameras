/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

/**
 * A CameraFilter implementation that filters Cameras by street.
 * @author dsale
 */
public class StreetMatcher implements CameraFilter {

	private static final Pattern streetExtractorPattern =
			Pattern.compile("^\\s*(?:\\d+\\s+)(?:[NSEW]\\s+)?(.*?)\\s*", Pattern.CASE_INSENSITIVE);
	
	// Immutable class, so these are
	// computed once, lazily.
	private Integer hashCode = null;
	private String toString = null;
	
	private final String street;

	/**
	 * Creates a DefaultStreetmatcher instance for a given
	 * street representation.
	 * @param street
	 */
	public StreetMatcher(String street) {
		notNull(street, "invalid parameter: street=" + street);
		notEmpty(street.trim(), "invalid parameter: street=" + street);
		this.street = street.trim().toUpperCase();
	}

	// this exists for the benefit of StreetMatcherFactory.streetMatcherAlways
	protected StreetMatcher() {
		this.street = null;
	}

	public String getStreet() {
		return street;
	}

	@Override
	public boolean accept(Camera camera) {
		notNull(camera, "invalid parameter: camera=" + camera);
		
		boolean match = false;
		
		if (camera instanceof SpeedCamera) {
			
			String cameraStreet = streetForAddress(((SpeedCamera) camera).getAddress());
			match = street.contains(cameraStreet.toUpperCase());
			
		} else if (camera instanceof RedLightCamera) {
			
			for (String cameraStreet : ((RedLightCamera)camera).getIntersection())
				if (street.contains(cameraStreet.toUpperCase()))
					match = true;
		}
		
		return match;
	}
	
	String streetForAddress(String address) {
		Matcher matcher = streetExtractorPattern.matcher(address);
		return matcher.matches()? matcher.group(1).replaceAll("\\s+", " ") : null;
	}
	
	@Override
	public boolean equals(Object object) {
	   if (object == null) return false;
	   if (object == this) return true;
	   if (object.getClass() != getClass()) return false;
	   
	   StreetMatcher that = (StreetMatcher) object;
	   
	   return new EqualsBuilder()
	                 .append(street, that.street)
	                 .isEquals();
	}

	@Override
	public int hashCode() {
		// immutable; compute hashCode once, lazily
		if (hashCode == null)
			hashCode = new HashCodeBuilder(19, 73)
					.append(street)
					.toHashCode();
		return hashCode;
	}

	@Override
	public String toString() {
		// immutable; generate toString once, lazily
		if (toString == null)
			toString = new ToStringBuilder(this)
				.append("street", street)
				.toString();

		return toString;
	}
}