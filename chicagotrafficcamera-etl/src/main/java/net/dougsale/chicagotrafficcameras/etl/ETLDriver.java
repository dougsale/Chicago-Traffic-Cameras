package net.dougsale.chicagotrafficcameras.etl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

import net.dougsale.chicagotrafficcameras.domain.Cameras;

/**
 * Reads the camera data from the 2 CSV files, transforming them into RedLightCamera and SpeedCamera
 * instances stored in a Cameras instance.  The Cameras instance and its contents are then serialized
 * to a file.
 * @author dsale
 */
public class ETLDriver {

	// input
	static String redLightCameraSource = "Red_Light_Camera_Locations.csv";
	static String speedCameraSource = "Speed_Camera_Locations.csv";
	
	// output
	static String serialzedClassOutputFile = "./Cameras.ser";

	public static void main(String[] args) {
		
		int rc = 0;

		Cameras cameras = new Cameras();

		// generate RedLightCamera instances from CSV, add to Cameras instance
		try (InputStreamReader source = new InputStreamReader(ETLDriver.class.getClassLoader().getResourceAsStream(redLightCameraSource))) {
			new RedLightCameraCsvExtractor().extract(source, cameras);
		} catch (IOException e) {
			System.err.println("Failed to extract/transform resource: " + redLightCameraSource);
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			rc++;
		}
		
		if (rc != 0) System.exit(rc);
		
		// generate SpeedCamera instances from CSV, add to Cameras instance
		try (InputStreamReader source = new InputStreamReader(ETLDriver.class.getClassLoader().getResourceAsStream(speedCameraSource))) {
			new SpeedCameraCsvExtractor().extract(source, cameras);
		} catch (IOException e) {
			System.err.println("Failed to extract/transform resource: " + speedCameraSource);
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			rc++;
		}
		
		if (rc != 0) System.exit(rc);
		
		// persist cameras to file as serialized objects 
		try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(serialzedClassOutputFile))) {
			stream.writeObject(cameras);
		} catch (IOException e) {
			System.err.println("Failed to transform/store resource: " + serialzedClassOutputFile);
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			rc++;
		}
	}
}
