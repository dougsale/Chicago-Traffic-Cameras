/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain.builders;

import java.util.ArrayList;
import java.util.List;

import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.Route.Step;

/**
 * @author dsale
 *
 */
public final class RouteBuilder {

	private RouteData data;
	
	/**
	 * 
	 */
	public RouteBuilder() {
		reset();
	}
	
	public void reset() {
		data = new RouteData();
	}
	
	public RouteBuilder withStartAddress(String startAddress) {
		data.setStartAddress(startAddress);
		return this;
	}
	
	public RouteBuilder withEndAddress(String endAddress) {
		data.setEndAddress(endAddress);
		return this;
	}
	
	public RouteBuilder withStep(Step step) {
		data.addStep(step);
		return this;
	}
	
	public RouteBuilder withStep(String instructions, Location start, Location end) {
		return withStep(new Step(instructions, start, end));
	}
	
	public RouteBuilder withStep(String instructions, double startLat, double startLong, double endLat, double endLong) {
		return withStep(instructions, new Location(startLat, startLong), new Location(endLat, endLong));
	}
	
	public Route build() {
		return new Route(data.getStartAddress(), data.getEndAddress(), data.getSteps());
	}

	RouteData getRouteData() {
		return data;
	}

	static class RouteData {
		private String startAddress = null;
		private String endAddress = null;
		private List<Step> steps = null;
		public String getStartAddress() {
			return startAddress;
		}
		public void setStartAddress(String startAddress) {
			this.startAddress = startAddress;
		}
		public String getEndAddress() {
			return endAddress;
		}
		public void setEndAddress(String endAddress) {
			this.endAddress = endAddress;
		}
		public List<Step> getSteps() {
			return steps;
		}
		public void addStep(Step step) {
			if (steps == null)
				steps = new ArrayList<>();
			steps.add(step);
		}
	}
}
