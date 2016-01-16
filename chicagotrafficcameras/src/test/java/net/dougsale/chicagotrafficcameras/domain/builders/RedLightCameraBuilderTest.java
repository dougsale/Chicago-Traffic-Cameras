package net.dougsale.chicagotrafficcameras.domain.builders;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Direction;
import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;

public class RedLightCameraBuilderTest {

	@Test
	public void testBuildWithObjects() {
		String street1 = "street1";
		String street2 = "street2";
		Location location = mock(Location.class);
		
		RedLightCamera camera = new RedLightCameraBuilder()
				.withStreet(street1)
				.withStreet(street2)
				.withLocation(location)
				.withApproach(Direction.WESTBOUND)
				.withApproach(Direction.EASTBOUND)
				.build();
		
		assertThat(camera, not(nullValue()));
		assertThat(camera.getIntersection(), equalTo(new HashSet<>(Arrays.asList(street1, street2))));
		assertThat(camera.getLocation(), equalTo(location));
		assertThat(camera.getApproaches(), equalTo(EnumSet.of(Direction.EASTBOUND, Direction.WESTBOUND)));
	}

	@Test
	public void testBuildWithPrimitives() {
		String street1 = "street1";
		String street2 = "street2";
		double latitude = 1.0;
		double longitude = 2.0;
		Location location = new Location(latitude, longitude);
		
		RedLightCamera camera = new RedLightCameraBuilder()
				.withStreet(street1)
				.withStreet(street2)
				.withLocation(latitude, longitude)
				.withApproach("WESTBOUND")
				.withApproach("EASTBOUND")
				.build();
		
		assertThat(camera, not(nullValue()));
		assertThat(camera.getIntersection(), equalTo(new HashSet<>(Arrays.asList(street1, street2))));
		assertThat(camera.getLocation(), equalTo(location));
		assertThat(camera.getApproaches(), equalTo(EnumSet.of(Direction.EASTBOUND, Direction.WESTBOUND)));
	}
	
	@Test
	public void testReset() {
		RedLightCameraBuilder builder = new RedLightCameraBuilder()
				.withStreet("street1")
				.withStreet("street2")
				.withLocation(mock(Location.class))
				.withApproach(Direction.NORTHBOUND);
		builder.reset();

		assertThat(builder.getCameraData().getIntersection(), nullValue());
		assertThat(builder.getCameraData().getLocation(), nullValue());
		assertThat(builder.getCameraData().getApproaches(), nullValue());
	}

}