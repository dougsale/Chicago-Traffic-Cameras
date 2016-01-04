/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain.builders;

import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

/**
 * @author dsale
 *
 */
public class SpeedCameraBuilder extends CameraBuilder {

	//TODO need to override methods that return CameraBuilder to provide fluent interface (smalltalk-style)
	
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
	public SpeedCamera build() {
		SpeedCameraData data = getCameraData();
		return new SpeedCamera(data.getAddress(), data.getLocation(), data.getApproaches());
	}
	
	class SpeedCameraData extends CameraData {
		private String address = null;
		public void setAddress(String address) {
			this.address = address;
		}
		public String getAddress() {
			return address;
		}
	}
}
