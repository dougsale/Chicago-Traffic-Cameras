/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain.builders;

import java.util.HashSet;
import java.util.Set;

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
		return withLocation(new Location(latitude, longitude));
	}
	
	public CameraBuilder withApproach(Direction approach) {		
		data.addApproach(approach);
		return this;
	}
	
	public CameraBuilder withApproach(String approach) {
		return withApproach(Direction.fromString(approach));
	}
	
	public Camera build() {
		return new Camera(data.getLocation(), data.getApproaches());
	}
	
	static class CameraData {
		private Location location = null;
		private Set<Direction> approaches = null;
		public Location getLocation() {
			return location;
		}
		public void setLocation(Location location) {
			this.location = location;
		}
		public void addApproach(Direction direction) {
			if (approaches == null)
				approaches = new HashSet<>();

			approaches.add(direction);
		}
		public Set<Direction> getApproaches() {
			return approaches;
		}
	}
}
