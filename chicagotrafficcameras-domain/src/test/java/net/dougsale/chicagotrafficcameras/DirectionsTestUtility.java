package net.dougsale.chicagotrafficcameras;

import static org.junit.Assert.fail;

import java.io.IOException;

import net.dougsale.chicagotrafficcameras.domain.mappers.DirectionsFromJsonMapper;

public class DirectionsTestUtility {
	
	private static DirectionsFromJsonMapper mapper = new DirectionsFromJsonMapper();
	
	public static Directions getDirections(String json) {
		Directions directions = null;
		try {
			directions = mapper.map(json);
		} catch (IOException e) {
			fail("directions parse failed: " + e.getMessage());
		}
		return directions; // for compiler
	}
}
