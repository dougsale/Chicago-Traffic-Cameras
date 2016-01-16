/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain.builders;

import java.util.HashSet;
import java.util.Set;

import net.dougsale.chicagotrafficcameras.domain.Direction;
import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;

/**
 * @author dsale
 *
 */
public class RedLightCameraBuilder extends CameraBuilder {

	/**
	 * 
	 */
	public RedLightCameraBuilder() {
		super();
	}

	@Override
	protected RedLightCameraData getCameraData() {
		return (RedLightCameraData) super.getCameraData();
	}

	@Override
	protected CameraData newCameraData() {
		return new RedLightCameraData();
	}
	
	public RedLightCameraBuilder withStreet(String street) {
		getCameraData().addStreet(street);
		return this;
	}
	
	@Override
	public RedLightCameraBuilder withLocation(Location location) {
		super.withLocation(location);
		return this;
	}
	
	@Override
	public RedLightCameraBuilder withLocation(double latitude, double longitude) {
		super.withLocation(latitude, longitude);
		return this;
	}
	
	@Override
	public RedLightCameraBuilder withApproach(Direction approach) {		
		super.withApproach(approach);
		return this;
	}
	@Override
	public RedLightCameraBuilder withApproach(String approach) {
		super.withApproach(approach);
		return this;
	}
		
	@Override
	public RedLightCamera build() {
		RedLightCameraData data = getCameraData();
		return new RedLightCamera(data.getIntersection(), data.getLocation(), data.getApproaches());
	}
	
	static class RedLightCameraData extends CameraData {
		private Set<String> intersection = null;
		public void addStreet(String street) {
			if (intersection == null)
				intersection = new HashSet<>();
			
			intersection.add(street);
		}
		public Set<String> getIntersection() {
			return intersection;
		}
	}
}
