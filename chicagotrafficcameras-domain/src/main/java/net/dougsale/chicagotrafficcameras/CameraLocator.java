/**
 * 
 */
package net.dougsale.chicagotrafficcameras;

/**
 * @author dsale
 *
 */
public class CameraLocator {

	private Cameras cameras;
	
	public CameraLocator(Cameras cameras) {
		this.cameras = cameras;
	}
	
	public Cameras locate(Directions directions) {
		Cameras found = cameras;
		
		// locate cameras here :)
		
		return found;
	}
}
