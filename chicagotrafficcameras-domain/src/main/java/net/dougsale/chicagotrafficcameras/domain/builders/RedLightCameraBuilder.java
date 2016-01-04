/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain.builders;

import java.util.HashSet;
import java.util.Set;

import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;

/**
 * @author dsale
 *
 */
public class RedLightCameraBuilder extends CameraBuilder {

	//TODO need to override methods that return CameraBuilder to provide fluent interface (smalltalk-style)

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
	public RedLightCamera build() {
		RedLightCameraData data = getCameraData();
		return new RedLightCamera(data.getIntersection(), data.getLocation(), data.getApproaches());
	}
	
	class RedLightCameraData extends CameraData {
		private Set<String> intersection = new HashSet<>();
		public void addStreet(String street) {
			intersection.add(street);
		}
		public Set<String> getIntersection() {
			return intersection;
		}
	}
}
