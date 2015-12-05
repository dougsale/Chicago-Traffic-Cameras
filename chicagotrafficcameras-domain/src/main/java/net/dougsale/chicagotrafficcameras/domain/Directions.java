/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Immutable data class representing the salient portion of the directions
 * provided by the Google Maps API.  These directions represent the steps
 * in a single leg of a driving journey with no waypoints.
 * 
 * @author dsale
 *
 */
public class Directions {

	public final String startAddress;
	public final String endAddress;
	public final List<Step> steps;

	public Directions(String startAddress, String endAddress, List<Step> steps) {
		this.startAddress = startAddress;
		this.endAddress = endAddress;
		this.steps = Collections.unmodifiableList(new ArrayList<Step>(steps));
	}

	static public class Step {
		
		public final String instructions;
		public final Location start;
		public final Location end;
		
		public Step(String instructions, Location start, Location end) {
			this.instructions = instructions;
			this.start = start;
			this.end = end;
		}

		@Override
		public String toString() {
			return 
				new ToStringBuilder(this)
					.appendSuper(super.toString())
					.append("instructions", instructions)
					.append("start", start)
					.append("end", end)
					.toString();
		}
	}
	
	@Override
	public String toString() {
		return 
			new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("startAddress", startAddress)
				.append("endAddress", endAddress)
				.append("steps", steps)
				.toString();
	}
}