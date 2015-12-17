package net.dougsale.chicagotrafficcameras;

import net.dougsale.chicagotrafficcameras.domain.Camera;

public interface CameraFilter {

	boolean accept(Camera camera);
}