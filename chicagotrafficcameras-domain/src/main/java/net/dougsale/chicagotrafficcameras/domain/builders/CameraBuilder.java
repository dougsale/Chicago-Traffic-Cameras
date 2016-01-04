/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain.builders;

import java.util.EnumSet;

import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.Direction;
import net.dougsale.chicagotrafficcameras.domain.Location;

/**
 * @author dsale
 *
 */
public class CameraBuilder {

	private CameraData data;
	
	/**
	 * 
	 */
	public CameraBuilder() {
		reset();
	}
	
	public final void reset() {
		data = newCameraData();
	}
	
	protected CameraData getCameraData() {
		return data;
	}

	protected CameraData newCameraData() {
		return new CameraData();
	}
	
	public CameraBuilder withLocation(Location location) {
		data.setLocation(location);
		return this;
	}
	
	public CameraBuilder withLocation(double latitude, double longitude) {
		data.setLocation(new Location(latitude, longitude));
		return this;
	}
	
	public CameraBuilder withApproach(Direction approach) {
		data.addApproach(approach);
		return this;
	}
	
	public Camera build() {
		return new Camera(data.getLocation(), data.getApproaches());
	}
	
	class CameraData {
		private Location location = null;
		private EnumSet<Direction> approaches = null;
		public Location getLocation() {
			return location;
		}
		public void setLocation(Location location) {
			this.location = location;
		}
		public void addApproach(Direction direction) {
			if (approaches == null)
				approaches = EnumSet.of(direction);
			else
				approaches.add(direction);
		}
		public EnumSet<Direction> getApproaches() {
			return approaches;
		}
	}
}
