/**
 * 
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.dougsale.chicagotrafficcameras.Route;
import net.dougsale.chicagotrafficcameras.Route.Step;
import static org.apache.commons.lang3.Validate.*;

/**
 * CompositeCameraFilterFactory is a CameraFilterFactory composed of
 * multiple CameraFilterFactories.  It generates CompositeCameraFilters
 * that embody the CameraFilters of its component CameraFilterFactory instances.
 * @author dsale
 */
public class CompositeCameraFilterFactory implements CameraFilterFactory {

	private final CameraFilterFactory[] factories;

	/**
	 * 
	 */
	public CompositeCameraFilterFactory(CameraFilterFactory... factories) {
		notNull(factories, "invalid parameter: factories=null");
		isTrue(!(factories.length < 2),
				String.format("invalid parameter: factories (length < 2); length=%d", factories.length));
		for (CameraFilterFactory factory : factories)
			notNull(factory, "invalid parameter: factories; contains element=null");
		
		this.factories = Arrays.copyOf(factories, factories.length);
	}
	
	public CameraFilterFactory[] getCameraFilterFactories() {
		return Arrays.copyOf(factories, factories.length);
	}

	/* (non-Javadoc)
	 * @see net.dougsale.chicagotrafficcameras.camerafilters.CameraFilterFactory#getCameraFilters(net.dougsale.chicagotrafficcameras.Route)
	 */
	@Override
	public Map<Step, CameraFilter> getCameraFilters(Route route) {
		notNull(route);
		
		List<Step> steps = route.getSteps();
		int numSteps = steps.size();
		
		CameraFilter[][] componentFilters = new CameraFilter[numSteps][factories.length];

		Map<Step,CameraFilter> filtersByStep;
		
		// for each factory, retrieve its filters-by-step and place them in the componentFilters array
		for (int j = 0; j < factories.length; j++) {
			// this is the expensive call, involves creating all the camera filters,
			// thus the inversion of the loops
			filtersByStep = factories[j].getCameraFilters(route);
			
			for (int i = 0; i < numSteps; i++) {
				componentFilters[i][j] = filtersByStep.get(steps.get(i));
			}
		}
		
		// create the data structure for the result
		filtersByStep = new HashMap<>(numSteps);
		CompositeCameraFilter filter;
		
		for (int i = 0; i < numSteps; i++) {
			CameraFilter[] filters = componentFilters[i];
			filter = new CompositeCameraFilter(filters);
			filtersByStep.put(steps.get(i), filter);
		}

		return filtersByStep;
	}
}
