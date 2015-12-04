/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain;

/**
 * @author dsale
 *
 */
public interface TrafficCameraLocationFilter {
	boolean accept(Camera cameraLocation);
}
