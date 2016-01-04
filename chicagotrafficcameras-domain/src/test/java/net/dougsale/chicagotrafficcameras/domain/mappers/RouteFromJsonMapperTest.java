/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain.mappers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.Route.Step;

public class RouteFromJsonMapperTest {

	RouteFromJsonMapper mapper = new RouteFromJsonMapper();
	
	@Test(expected=NullPointerException.class)
	public void testMapNullJson() throws JsonParseException, JsonMappingException, IOException {
		mapper.map(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testMapEmptyJson() throws JsonParseException, JsonMappingException, IOException {
		mapper.map("  ");
	}

	@Test
	public void testMap() throws JsonParseException, JsonMappingException, IOException {
		String json = "{\"startAddress\":\"716 S Central Park Ave, Chicago, IL 60624, USA\",\"endAddress\":\"401 N Central Park Ave, Chicago, IL 60624, USA\",\"steps\":[{\"instructions\":\"Head <b>north</b> on <b>S Central Park Ave</b> toward <b>W Flournoy St</b>\",\"start\":{\"latitude\":41.8721333,\"longitude\":-87.71559030000003},\"end\":{\"latitude\":41.8848491,\"longitude\":-87.7168211}},{\"instructions\":\"Continue onto <b>N Conservatory Dr</b>\",\"start\":{\"latitude\":41.8848491,\"longitude\":-87.7168211},\"end\":{\"latitude\":41.887812,\"longitude\":-87.71656229999996}},{\"instructions\":\"Continue onto <b>N Central Park Ave</b>\",\"start\":{\"latitude\":41.887812,\"longitude\":-87.71656229999996},\"end\":{\"latitude\":41.8884337,\"longitude\":-87.71657859999999}},{\"instructions\":\"Turn <b>right</b> to stay on <b>N Central Park Ave</b><div style=\\\"font-size:0.9em\\\">Destination will be on the right</div>\",\"start\":{\"latitude\":41.8884337,\"longitude\":-87.71657859999999},\"end\":{\"latitude\":41.8885286,\"longitude\":-87.7162669}}]}";
		
		String startAddress = "716 S Central Park Ave, Chicago, IL 60624, USA";
		String endAddress = "401 N Central Park Ave, Chicago, IL 60624, USA";
		List<Step> steps = Arrays.asList(
			new Step("Head <b>north</b> on <b>S Central Park Ave</b> toward <b>W Flournoy St</b>", new Location(41.8721333, -87.71559030000003), new Location(41.8848491, -87.7168211)),
			new Step("Continue onto <b>N Conservatory Dr</b>", new Location(41.8848491, -87.7168211), new Location(41.887812, -87.71656229999996)),
			new Step("Continue onto <b>N Central Park Ave</b>", new Location(41.887812, -87.71656229999996), new Location(41.8884337, -87.71657859999999)),
			new Step("Turn <b>right</b> to stay on <b>N Central Park Ave</b><div style=\"font-size:0.9em\">Destination will be on the right</div>", new Location(41.8884337, -87.71657859999999), new Location(41.8885286, -87.7162669))
		);
		
		Route route = new Route(startAddress, endAddress, steps);
		
		assertThat(mapper.map(json), equalTo(route));
	}
}
