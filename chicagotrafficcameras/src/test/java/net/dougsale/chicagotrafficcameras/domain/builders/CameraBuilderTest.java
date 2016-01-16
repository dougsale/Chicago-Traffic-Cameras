package net.dougsale.chicagotrafficcameras.domain.builders;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.EnumSet;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.Direction;
import net.dougsale.chicagotrafficcameras.domain.Location;

public class CameraBuilderTest {

	@Test
	public void testBuildWithObjects() {
		Location location = mock(Location.class);
		
		Camera camera = new CameraBuilder()
				.withLocation(location)
				.withApproach(Direction.WESTBOUND)
				.withApproach(Direction.EASTBOUND)
				.build();
		
		assertThat(camera, not(nullValue()));
		assertThat(camera.getLocation(), equalTo(location));
		assertThat(camera.getApproaches(), equalTo(EnumSet.of(Direction.EASTBOUND, Direction.WESTBOUND)));
	}

	@Test
	public void testBuildWithPrimitives() {
		double latitude = 1.0;
		double longitude = 2.0;
		Location location = new Location(latitude, longitude);
		
		Camera camera = new CameraBuilder()
				.withLocation(latitude, longitude)
				.withApproach("WESTBOUND")
				.withApproach("EASTBOUND")
				.build();
		
		assertThat(camera, not(nullValue()));
		assertThat(camera.getLocation(), equalTo(location));
		assertThat(camera.getApproaches(), equalTo(EnumSet.of(Direction.EASTBOUND, Direction.WESTBOUND)));
	}
	
	@Test
	public void testReset() {
		CameraBuilder builder = new CameraBuilder()
				.withLocation(mock(Location.class))
				.withApproach(Direction.NORTHBOUND);
		builder.reset();

		assertThat(builder.getCameraData().getLocation(), nullValue());
		assertThat(builder.getCameraData().getApproaches(), nullValue());
	}

}