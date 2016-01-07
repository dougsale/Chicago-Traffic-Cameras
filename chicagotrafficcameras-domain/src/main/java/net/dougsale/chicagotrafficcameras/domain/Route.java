/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import net.dougsale.chicagotrafficcameras.domain.Location;

/**
 * Immutable data class representing the salient portion of the route
 * provided by the Google Maps API.  These route represent the steps
 * in a single leg of a driving journey with no waypoints.
 * 
 * @author dsale
 *
 */
public class Route implements Serializable {

	private static final long serialVersionUID = 10L;
	
	// immutable class, these values are computed once, lazily
	// current values indicate that they have not been computed
	private transient Integer hashCode = null;
	private String toString = null;

	private final String startAddress;
	private final String endAddress;
	private final List<Step> steps;

	public Route(String startAddress, String endAddress, List<Step> steps) {
		notNull(startAddress, "invalid parameter: startAddress=" + startAddress);
		notEmpty(startAddress.trim(), "invalid parameter: startAddress=" + startAddress);
		notNull(endAddress, "invalid parameter: endAddress=" + endAddress);
		notEmpty(endAddress.trim(), "invalid parameter: endAddress=" + endAddress);
		notEmpty(steps, "invalid parameter: steps; size=" + steps.size());
		for (Step step : steps)
			notNull(step, "invalid parameter: steps; contains element=" + step);

		this.startAddress = startAddress;
		this.endAddress = endAddress;
		this.steps = Collections.unmodifiableList(new ArrayList<Step>(steps));
	}

	public String getStartAddress() {
		return startAddress;
	}

	public String getEndAddress() {
		return endAddress;
	}

	public List<Step> getSteps() {
		return steps;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (object == this) return true;
		if (object.getClass() != getClass()) return false;

		Route that = (Route) object;
   
		return new EqualsBuilder()
			.append(startAddress, that.startAddress)
			.append(endAddress, that.endAddress)
			.append(steps, that.steps)
			.isEquals();
	}
	
	@Override
	public int hashCode() {
		if (hashCode == null)
			hashCode = new HashCodeBuilder(19, 73)
					.append(startAddress)
					.append(endAddress)
					.append(steps)
					.toHashCode();
		return hashCode;
	}
	
	@Override
	public String toString() {
		if (toString == null)
			toString = new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("startAddress", startAddress)
				.append("endAddress", endAddress)
				.append("steps", steps)
				.toString();
		return toString;
	}

	public static class Step {

		// immutable class, these values are computed once, lazily
		// current values indicate that they have not been computed
		private transient Integer hashCode = null;
		private String toString = null;

		private final String instructions;
		private final Location start;
		private final Location end;
		
		public Step(String instructions, Location start, Location end) {
			notNull(instructions, "invalid parameter: instructions=" + instructions);
			notEmpty(instructions.trim(), "invalid parameter: instructions=" + instructions);
			notNull(start, "invalid parameter: start=" + start);
			notNull(end, "invalid parameter: end=" + end);
			
			this.instructions = instructions;
			this.start = start;
			this.end = end;
		}

		public String getInstructions() {
			return instructions;
		}

		public Location getStart() {
			return start;
		}

		public Location getEnd() {
			return end;
		}
		
		@Override
		public boolean equals(Object object) {
			if (object == null) return false;
			if (object == this) return true;
			if (object.getClass() != getClass()) return false;

			Step that = (Step) object;
	   
			return new EqualsBuilder()
				.append(instructions, that.instructions)
				.append(start, that.start)
				.append(end, that.end)
				.isEquals();
		}
		
		@Override
		public int hashCode() {
			if (hashCode == null)
				hashCode = new HashCodeBuilder(19, 73)
					.append(instructions)
					.append(start)
					.append(end)
					.toHashCode();
			return hashCode;
		}

		@Override
		public String toString() {
			if (toString == null)
				toString = new ToStringBuilder(this)
					.appendSuper(super.toString())
					.append("instructions", instructions)
					.append("start", start)
					.append("end", end)
					.toString();
			return toString;
		}
	}
}