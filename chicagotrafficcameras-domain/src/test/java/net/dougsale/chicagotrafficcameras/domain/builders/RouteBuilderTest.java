package net.dougsale.chicagotrafficcameras.domain.builders;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.Route.Step;

public class RouteBuilderTest {

	@Test
	public void testBuild() {
		
		String startAddress = "startAddress";
		String endAddress = "endAddress";
		
		// withStep(step)
		Step step1 = mock(Step.class);

		// withStep(instructions, start, end)
		String instruction2 = "instruction2";
		Location start = mock(Location.class);
		Location end = mock(Location.class);
		Step step2 = new Step(instruction2, start, end);
		
		// withStep(instructions, startLat, startLong, endLat, endLong)
		String instruction3 = "instruction3";
		double startLat = 0.0, startLong = 0.0, endLat = 0.3, endLong = 0.3;
		Step step3 = new Step(instruction3, new Location(startLat, startLong), new Location(endLat, endLong));
		
		Route route = new RouteBuilder()
				.withStartAddress(startAddress)
				.withEndAddress(endAddress)
				.withStep(step1)
				.withStep(instruction2, start, end)
				.withStep(instruction3, startLat, startLong, endLat, endLong)
				.build();
		
		assertThat(route, not(nullValue()));
		assertThat(route.getStartAddress(), equalTo(startAddress));
		assertThat(route.getEndAddress(), equalTo(endAddress));
		assertThat(route.getSteps(), equalTo(Arrays.asList(step1, step2, step3)));
	}
	
	@Test
	public void testReset() {
		RouteBuilder builder = new RouteBuilder()
				.withStartAddress("startAddress")
				.withEndAddress("endAddress")
				.withStep(mock(Step.class));
		builder.reset();

		assertThat(builder.getRouteData().getStartAddress(), nullValue());
		assertThat(builder.getRouteData().getEndAddress(), nullValue());
		assertThat(builder.getRouteData().getSteps(), nullValue());
	}

}
