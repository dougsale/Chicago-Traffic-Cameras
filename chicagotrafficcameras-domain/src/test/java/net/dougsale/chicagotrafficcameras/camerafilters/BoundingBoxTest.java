/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.Route;
import net.dougsale.chicagotrafficcameras.Route.Step;
import net.dougsale.chicagotrafficcameras.domain.Approach;
import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

public class BoundingBoxTest {

	
	@Test(expected=NullPointerException.class)
	public void testBoundingBoxNullStep() {
		new BoundingBox(null, 0.01);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testBoundingBoxInvalidPadding() {
		new BoundingBox(new Route.Step("instructions", mock(Location.class), mock(Location.class)), -0.01);
	}

	Location cameraLocation = new Location(41.867401834997956, -87.72508368792016);
	SpeedCamera spCamera = new SpeedCamera("address", cameraLocation, EnumSet.of(Approach.WESTBOUND));
	RedLightCamera rlCamera = new RedLightCamera(
		new HashSet<>(Arrays.asList("street1", "street2")), cameraLocation, EnumSet.of(Approach.WESTBOUND));
	
	@Test
	public void testInBounds() {
		double delta = 0.0011;
		double padding = 0.001;
		{ // box (excluding padding) is a point, the same as the camera
			Location stepStart = new Location(cameraLocation.latitude, cameraLocation.longitude);
			Location stepEnd = new Location(cameraLocation.latitude, cameraLocation.longitude);
			Step step = new Step("instructions", stepStart, stepEnd);
			BoundingBox box = new BoundingBox(step, padding);
			assertThat(box.inBounds(spCamera), is(true));
			assertThat(box.inBounds(rlCamera), is(true));
		}
		{ // box (excluding padding) is a point shifted south of the camera, but within the padding value
			Location stepStart = new Location(cameraLocation.latitude - padding, cameraLocation.longitude);
			Location stepEnd = new Location(cameraLocation.latitude - padding, cameraLocation.longitude);
			Step step = new Step("instructions", stepStart, stepEnd);
			BoundingBox box = new BoundingBox(step, padding);
			assertThat(box.inBounds(spCamera), is(true));
			assertThat(box.inBounds(rlCamera), is(true));
		}
		{ // box (excluding padding) is a point shifted east of the camera, but within the padding value
			Location stepStart = new Location(cameraLocation.latitude, cameraLocation.longitude - padding);
			Location stepEnd = new Location(cameraLocation.latitude, cameraLocation.longitude - padding);
			Step step = new Step("instructions", stepStart, stepEnd);
			BoundingBox box = new BoundingBox(step, padding);
			assertThat(box.inBounds(spCamera), is(true));
			assertThat(box.inBounds(rlCamera), is(true));
		}
		{ // box (excluding padding) is a point shifted south and east of the camera, but within the padding value
			Location stepStart = new Location(cameraLocation.latitude - padding, cameraLocation.longitude - padding);
			Location stepEnd = new Location(cameraLocation.latitude - padding, cameraLocation.longitude - padding);
			Step step = new Step("instructions", stepStart, stepEnd);
			BoundingBox box = new BoundingBox(step, padding);
			assertThat(box.inBounds(spCamera), is(true));
			assertThat(box.inBounds(rlCamera), is(true));
		}
		{ // box (excluding padding) is a point shifted north of the camera, not within the padding value
			Location stepStart = new Location(cameraLocation.latitude + delta, cameraLocation.longitude);
			Location stepEnd = new Location(cameraLocation.latitude + delta, cameraLocation.longitude);
			Step step = new Step("instructions", stepStart, stepEnd);
			BoundingBox box = new BoundingBox(step, padding);
			assertThat(box.inBounds(spCamera), is(false));
			assertThat(box.inBounds(rlCamera), is(false));
		}
		{ // box (excluding padding) is a point shifted west of the camera, not within the padding value
			Location stepStart = new Location(cameraLocation.latitude, cameraLocation.longitude + delta);
			Location stepEnd = new Location(cameraLocation.latitude, cameraLocation.longitude + delta);
			Step step = new Step("instructions", stepStart, stepEnd);
			BoundingBox box = new BoundingBox(step, padding);
			assertThat(box.inBounds(spCamera), is(false));
			assertThat(box.inBounds(rlCamera), is(false));
		}
		{ // box (excluding padding) is a point shifted north and west of the camera, not within the padding value
			Location stepStart = new Location(cameraLocation.latitude + delta, cameraLocation.longitude + delta);
			Location stepEnd = new Location(cameraLocation.latitude + delta, cameraLocation.longitude + delta);
			Step step = new Step("instructions", stepStart, stepEnd);
			BoundingBox box = new BoundingBox(step, padding);
			assertThat(box.inBounds(spCamera), is(false));
			assertThat(box.inBounds(rlCamera), is(false));
		}
	}

	@Test
	public void testAccept() {
		double delta = 0.0011;
		double padding = 0.001;
		{ // box (excluding padding) is a point, the same as the camera
			Location stepStart = new Location(cameraLocation.latitude, cameraLocation.longitude);
			Location stepEnd = new Location(cameraLocation.latitude, cameraLocation.longitude);
			Step step = new Step("instructions", stepStart, stepEnd);
			BoundingBox box = new BoundingBox(step, padding);
			assertThat(box.accept(spCamera), is(true));
			assertThat(box.accept(rlCamera), is(true));
		}
		{ // box (excluding padding) is a point shifted south of the camera, but within the padding value
			Location stepStart = new Location(cameraLocation.latitude - padding, cameraLocation.longitude);
			Location stepEnd = new Location(cameraLocation.latitude - padding, cameraLocation.longitude);
			Step step = new Step("instructions", stepStart, stepEnd);
			BoundingBox box = new BoundingBox(step, padding);
			assertThat(box.accept(spCamera), is(true));
			assertThat(box.accept(rlCamera), is(true));
		}
		{ // box (excluding padding) is a point shifted east of the camera, but within the padding value
			Location stepStart = new Location(cameraLocation.latitude, cameraLocation.longitude - padding);
			Location stepEnd = new Location(cameraLocation.latitude, cameraLocation.longitude - padding);
			Step step = new Step("instructions", stepStart, stepEnd);
			BoundingBox box = new BoundingBox(step, padding);
			assertThat(box.accept(spCamera), is(true));
			assertThat(box.accept(rlCamera), is(true));
		}
		{ // box (excluding padding) is a point shifted south and east of the camera, but within the padding value
			Location stepStart = new Location(cameraLocation.latitude - padding, cameraLocation.longitude - padding);
			Location stepEnd = new Location(cameraLocation.latitude - padding, cameraLocation.longitude - padding);
			Step step = new Step("instructions", stepStart, stepEnd);
			BoundingBox box = new BoundingBox(step, padding);
			assertThat(box.accept(spCamera), is(true));
			assertThat(box.accept(rlCamera), is(true));
		}
		{ // box (excluding padding) is a point shifted north of the camera, not within the padding value
			Location stepStart = new Location(cameraLocation.latitude + delta, cameraLocation.longitude);
			Location stepEnd = new Location(cameraLocation.latitude + delta, cameraLocation.longitude);
			Step step = new Step("instructions", stepStart, stepEnd);
			BoundingBox box = new BoundingBox(step, padding);
			assertThat(box.accept(spCamera), is(false));
			assertThat(box.accept(rlCamera), is(false));
		}
		{ // box (excluding padding) is a point shifted west of the camera, not within the padding value
			Location stepStart = new Location(cameraLocation.latitude, cameraLocation.longitude + delta);
			Location stepEnd = new Location(cameraLocation.latitude, cameraLocation.longitude + delta);
			Step step = new Step("instructions", stepStart, stepEnd);
			BoundingBox box = new BoundingBox(step, padding);
			assertThat(box.accept(spCamera), is(false));
			assertThat(box.accept(rlCamera), is(false));
		}
		{ // box (excluding padding) is a point shifted north and west of the camera, not within the padding value
			Location stepStart = new Location(cameraLocation.latitude + delta, cameraLocation.longitude + delta);
			Location stepEnd = new Location(cameraLocation.latitude + delta, cameraLocation.longitude + delta);
			Step step = new Step("instructions", stepStart, stepEnd);
			BoundingBox box = new BoundingBox(step, padding);
			assertThat(box.accept(spCamera), is(false));
			assertThat(box.accept(rlCamera), is(false));
		}
	}
}