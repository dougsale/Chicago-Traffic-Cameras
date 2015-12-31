/**
 * 
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.apache.commons.lang3.Validate.*;

import java.util.HashMap;
import java.util.Map;

import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.Route.Step;

/**
 * @author dsale
 *
 */
public class BoundingBoxFactory implements CameraFilterFactory {
	//
	// possibly more here to do in the future...
	// limiting redlightcameras to the step where the user is approaching the intersection
	// i.e., not the next step where the user is leaving the intersection
	// or limiting cameras where the termination of the route is prior to the intersection
	//
	
	private final double padding;
	
	/**
	 * <p>
	 * A CameraFilterFactory that creates CameraFilter instances that generate
	 * a bounding box based on the start and stop locations of the step in
	 * the route and the padding parameter.
	 * </p>
	 * <p>
	 * Note that the padding is in degrees and thus isn't equal in absolute
	 * distance between latitude and longitude.  Over small padding values
	 * this is considered negligible.
	 * </p>
	 * @param padding
	 * @throws IllegalArgumentException if padding < 0
	 */
	public BoundingBoxFactory(double padding) {
		isTrue(padding >= 0.0, "invalid parameter: padding=" + padding + " (must be >= 0.0)");
		this.padding = padding;
	}

	public double getPadding() {
		return padding;
	}
	
	/**
	 * @param step a Step in the current Route
	 * @return a BoundingBox implementation of CameraFilter
	 * @throws NullPointerExceptin if step parameter is null
	 * @throws IllegalStateException if route member is null
	 */
	@Override
	public Map<Step,CameraFilter> getCameraFilters(Route route) {
		notNull(route, "invalid parameter: route=null");
		
		Map<Step,CameraFilter> filters = new HashMap<>(route.getSteps().size());
		
		Location start, end, min, max;
		BoundingBox box;
		
		for (Step step : route.getSteps()) {
			start = step.getStart();
			end = step.getEnd();
	
			min = new Location(
				Math.min(start.latitude, end.latitude) - padding,
				Math.min(start.longitude, end.longitude) - padding);
		
			max = new Location(
				Math.max(start.latitude, end.latitude) + padding,
				Math.max(start.longitude, end.longitude) + padding);
		
			box = new BoundingBox(min, max);
		
			filters.put(step, box);
		}
		
		return filters;
	}
}