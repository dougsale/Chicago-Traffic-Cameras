/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain.builders;

import net.dougsale.chicagotrafficcameras.domain.Direction;
import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

/**
 * @author dsale
 *
 */
public class SpeedCameraBuilder extends CameraBuilder {

	/**
	 * 
	 */
	public SpeedCameraBuilder() {
		super();
	}

	@Override
	protected SpeedCameraData getCameraData() {
		return (SpeedCameraData) super.getCameraData();
	}

	@Override
	protected CameraData newCameraData() {
		return new SpeedCameraData();
	}
	
	public SpeedCameraBuilder withAddress(String address) {
		getCameraData().setAddress(address);
		return this;
	}
	
	@Override
	public SpeedCameraBuilder withLocation(Location location) {
		super.withLocation(location);
		return this;
	}
	
	@Override
	public SpeedCameraBuilder withLocation(double latitude, double longitude) {
		super.withLocation(latitude, longitude);
		return this;
	}
	
	@Override
	public SpeedCameraBuilder withApproach(Direction approach) {		
		super.withApproach(approach);
		return this;
	}
	@Override
	public SpeedCameraBuilder withApproach(String approach) {
		super.withApproach(approach);
		return this;
	}
		
	@Override
	public SpeedCamera build() {
		SpeedCameraData data = getCameraData();
		return new SpeedCamera(data.getAddress(), data.getLocation(), data.getApproaches());
	}
	
	static class SpeedCameraData extends CameraData {
		private String address = null;
		public void setAddress(String address) {
			this.address = address;
		}
		public String getAddress() {
			return address;
		}
	}
}
