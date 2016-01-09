/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain.mappers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.builders.RouteBuilder;

public class RouteFromJsonMapperTest {

	RouteFromJsonMapper mapper = new RouteFromJsonMapper();
	
	@Test
	public void testMap() throws MapperException {
		String json = "{\"startAddress\":\"716 S Central Park Ave, Chicago, IL 60624, USA\",\"endAddress\":\"401 N Central Park Ave, Chicago, IL 60624, USA\",\"steps\":[{\"instructions\":\"Head <b>north</b> on <b>S Central Park Ave</b> toward <b>W Flournoy St</b>\",\"start\":{\"latitude\":41.8721333,\"longitude\":-87.71559030000003},\"end\":{\"latitude\":41.8848491,\"longitude\":-87.7168211}},{\"instructions\":\"Continue onto <b>N Conservatory Dr</b>\",\"start\":{\"latitude\":41.8848491,\"longitude\":-87.7168211},\"end\":{\"latitude\":41.887812,\"longitude\":-87.71656229999996}},{\"instructions\":\"Continue onto <b>N Central Park Ave</b>\",\"start\":{\"latitude\":41.887812,\"longitude\":-87.71656229999996},\"end\":{\"latitude\":41.8884337,\"longitude\":-87.71657859999999}},{\"instructions\":\"Turn <b>right</b> to stay on <b>N Central Park Ave</b><div style=\\\"font-size:0.9em\\\">Destination will be on the right</div>\",\"start\":{\"latitude\":41.8884337,\"longitude\":-87.71657859999999},\"end\":{\"latitude\":41.8885286,\"longitude\":-87.7162669}}]}";
		
		Route route = new RouteBuilder()
				.withStartAddress("716 S Central Park Ave, Chicago, IL 60624, USA")
				.withEndAddress("401 N Central Park Ave, Chicago, IL 60624, USA")
				.withStep("Head <b>north</b> on <b>S Central Park Ave</b> toward <b>W Flournoy St</b>", 41.8721333, -87.71559030000003, 41.8848491, -87.7168211)
				.withStep("Continue onto <b>N Conservatory Dr</b>", 41.8848491, -87.7168211, 41.887812, -87.71656229999996)
				.withStep("Continue onto <b>N Central Park Ave</b>", 41.887812, -87.71656229999996, 41.8884337, -87.71657859999999)
				.withStep("Turn <b>right</b> to stay on <b>N Central Park Ave</b><div style=\"font-size:0.9em\">Destination will be on the right</div>", 41.8884337, -87.71657859999999, 41.8885286, -87.7162669)
				.build();
		
		assertThat(mapper.map(json), equalTo(route));
	}
	
	@Test(expected=NullPointerException.class)
	public void testMapNullJson() throws MapperException {
		mapper.map(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testMapEmptyJson() throws MapperException {
		mapper.map("  ");
	}

	@Test(expected=MapperException.class)
	public void testMapMalformedJson() throws MapperException {
		mapper.map("{\"startAddress\":\"716 S Central Park Ave, Chicago, IL 60624, USA\",\"endAddress\":");
	}
}
