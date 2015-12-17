package net.dougsale.chicagotrafficcameras.etl;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

import net.dougsale.chicagotrafficcameras.Cameras;
import net.dougsale.chicagotrafficcameras.webapp.CamerasToJsonSerializer;

/**
 * Drives the ETL process for Chicago traffic cameras.
 * Reads the data from the provided CSV files.
 * Outputs serialized Cameras instance by default (./Cameras.ser).
 * Accepts one command line argument, the output format: "serializedClass" (the default), or "json"
 * @author dsale
 */
public class ETLDriver {

	static String redLightCameraSource = "Red_Light_Camera_Locations.csv";
	static String speedCameraSource = "Speed_Camera_Locations.csv";
	static String serialzedClassOutputFile = "./Cameras.ser";
	static String jsonOutputFile = "./Cameras.json";
	static List<String> outputFormats = Arrays.asList("serializedClass", "json");
		
	public static void main(String[] args) {
		
		int rc = 0;

		String outputFormat = (args.length == 0)? "serializedClass" : args[0];
		if (!outputFormats.contains(outputFormat)) {
			System.err.println("Unknown output format: " + outputFormat + "; [\"serializedClass\", \"json\"]");
			rc++;
			System.exit(rc);
		}
			
		Cameras cameras = new Cameras();
		
		//EXTRACT / TRANSFORM
		
		try (InputStreamReader source = new InputStreamReader(ETLDriver.class.getClassLoader().getResourceAsStream(redLightCameraSource))) {
			new RedLightCameraCsvExtractor().extract(source, cameras);
		} catch (IOException e) {
			System.err.println("Failed to extract/transform resource: " + redLightCameraSource);
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			rc++;
		}
		
		if (rc != 0) System.exit(rc);
		
		try (InputStreamReader source = new InputStreamReader(ETLDriver.class.getClassLoader().getResourceAsStream(speedCameraSource))) {
			new SpeedCameraCsvExtractor().extract(source, cameras);
		} catch (IOException e) {
			System.err.println("Failed to extract/transform resource: " + speedCameraSource);
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			rc++;
		}
		
		if (rc != 0) System.exit(rc);
		
		//TRANSFORM / LOAD
		
		if (outputFormat.equals(outputFormats.get(0))) {
			try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(serialzedClassOutputFile))) {
				stream.writeObject(cameras);
			} catch (IOException e) {
				System.err.println("Failed to transform/store resource: " + serialzedClassOutputFile);
				System.err.println(e.getMessage());
				e.printStackTrace(System.err);
				rc++;
			}
		} else if (outputFormat.equals(outputFormats.get(1))) {
			try (FileWriter writer = new FileWriter(jsonOutputFile)) {
				writer.write(new CamerasToJsonSerializer().serialize(cameras));
			} catch (IOException e) {
				System.err.println("Failed to transform/store resource: " + jsonOutputFile);
				System.err.println(e.getMessage());
				e.printStackTrace(System.err);
				rc++;
			}
		}
	}
}
