/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras;

import static org.junit.Assert.fail;

import java.io.IOException;

import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.mappers.RouteFromJsonMapper;

public class TestUtility {
	
	private static RouteFromJsonMapper mapper = new RouteFromJsonMapper();
	
	public static Route getRoute(String json) {
		Route route = null;
		try {
			route = mapper.map(json);
		} catch (IOException e) {
			fail("route parse failed: " + e.getMessage());
		}
		return route; // for compiler
	}
}
