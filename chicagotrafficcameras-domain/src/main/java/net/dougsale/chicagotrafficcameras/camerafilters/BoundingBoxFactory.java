/**
 * 
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.apache.commons.lang3.Validate.isTrue;

import net.dougsale.chicagotrafficcameras.Route;
import net.dougsale.chicagotrafficcameras.Route.Step;

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
	private Route route = null;
	
	public BoundingBoxFactory(double padding) {
		isTrue(padding >= 0.0, "invalid parameter: padding=" + padding + " (must be >= 0.0)");
		this.padding = padding;
	}

	@Override
	public void setRoute(Route route) {
		this.route = route;		
	}

	@Override
	public CameraFilter getCameraFilter(Step step) {
		return getBoundingBox(step);
	}
	
	public BoundingBox getBoundingBox(Step step) {
		return new BoundingBox(step, padding);
	}
	
	public double getPadding() {
		return padding;
	}
}