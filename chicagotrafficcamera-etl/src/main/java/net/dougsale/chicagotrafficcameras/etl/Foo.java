package net.dougsale.chicagotrafficcameras.etl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import net.dougsale.chicagotrafficcameras.Cameras;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

public class Foo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream("./Cameras.ser"))) {
			Cameras cameras = (Cameras) stream.readObject();
			int i = 0;
			for (SpeedCamera camera : cameras.get(SpeedCamera.class))
				System.out.println(++i + ": " + camera);
			i = 0;
			for (RedLightCamera camera : cameras.get(RedLightCamera.class))
				System.out.println(++i + ": " + camera);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
