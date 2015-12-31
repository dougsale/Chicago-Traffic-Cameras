/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.Location;

public class BoundingBoxTest {
	
	@Test
	public void testBoundingBox() {
		Location min = new Location(0.0, 0.0);
		Location max = new Location(0.0001, 0.0001);
		BoundingBox box = new BoundingBox(min, max);
		assertThat(box.min, equalTo(min));
		assertThat(box.max, equalTo(max));
	}

	@Test(expected=NullPointerException.class)
	public void testBoundingBoxNullMin() {
		new BoundingBox(null, mock(Location.class));
	}

	@Test(expected=NullPointerException.class)
	public void testBoundingBoxNullMax() {
		new BoundingBox(mock(Location.class), null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBoundingBoxIllegalMinMaxLat() {
		Location min = new Location(0.0001, 0.0);
		Location max = new Location(0.0, 0.0);
		new BoundingBox(min, max);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testBoundingBoxIllegalMinMaxLong() {
		Location min = new Location(0.0, 0.0001);
		Location max = new Location(0.0, 0.0);
		new BoundingBox(min, max);
	}
	
	@Test
	public void testAccept() {

		Location cameraLocation = new Location(41.867, -87.725);
		Camera camera = when(mock(Camera.class).getLocation()).thenReturn(cameraLocation).getMock();
		
		double delta = 0.001;
		
		{ // box centered on camera
			Location min = new Location(cameraLocation.latitude - delta, cameraLocation.longitude - delta);
			Location max = new Location(cameraLocation.latitude + delta, cameraLocation.longitude + delta);
			BoundingBox box = new BoundingBox(min, max);
			assertThat(box.accept(camera), is(true));
		}
		{ // box with camera on it's NW corner
			Location min = new Location(cameraLocation.latitude - delta, cameraLocation.longitude - delta);
			Location max = new Location(cameraLocation.latitude, cameraLocation.longitude);
			BoundingBox box = new BoundingBox(min, max);
			assertThat(box.accept(camera), is(true));
		}
		{ // box as a point, the same as the camera
			Location min = new Location(cameraLocation.latitude, cameraLocation.longitude);
			Location max = new Location(cameraLocation.latitude, cameraLocation.longitude);
			BoundingBox box = new BoundingBox(min, max);
			assertThat(box.accept(camera), is(true));
		}
		{ // box not containing camera
			Location min = new Location(cameraLocation.latitude - 2 * delta, cameraLocation.longitude - 2 * delta);
			Location max = new Location(cameraLocation.latitude - delta, cameraLocation.longitude - delta);
			BoundingBox box = new BoundingBox(min, max);
			assertThat(box.accept(camera), is(false));
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void testAcceptNullCamera() {
		new BoundingBox(mock(Location.class), mock(Location.class)).accept(null);
	}
	
	@Test
	public void testFilter() {	
		Location min = new Location(-1.0, -1.0);
		Location max = new Location(1.0, 1.0);
		BoundingBox box = new BoundingBox(min, max);
		
		Camera
			ok1 = when(mock(Camera.class).getLocation()).thenReturn(min).getMock(),
			ok2 = when(mock(Camera.class).getLocation()).thenReturn(max).getMock(),
			ok3 = when(mock(Camera.class).getLocation()).thenReturn(new Location(0.0, 0.0)).getMock(),
			nope1 = when(mock(Camera.class).getLocation()).thenReturn(new Location(2.0, 0.0)).getMock(),
			nope2 = when(mock(Camera.class).getLocation()).thenReturn(new Location(0.0, 2.0)).getMock(),
			nope3 = when(mock(Camera.class).getLocation()).thenReturn(new Location(1.0000001, 0.0)).getMock(),
			nope4 = when(mock(Camera.class).getLocation()).thenReturn(new Location(0.0, 1.000001)).getMock();
		
		Set<Camera> cameraSet = new HashSet<>(Arrays.asList(ok1, ok2, ok3, nope1, nope2, nope3, nope4));
		Cameras source = when(mock(Cameras.class).get()).thenReturn(cameraSet).getMock();
		
		Cameras result = box.filter(source);
		
		assertThat(result, sameInstance(source));
		
		verify(result).remove(nope1);
		verify(result).remove(nope2);
		verify(result).remove(nope3);
		verify(result).remove(nope4);

		verify(result, never()).remove(ok1);
		verify(result, never()).remove(ok2);
		verify(result, never()).remove(ok3);
	}
	
	@Test(expected=NullPointerException.class)
	public void testFilterNullCameras() {
		new BoundingBox(mock(Location.class), mock(Location.class)).filter(null);
	}

	@Test
	public void testEqualsHashCode() {
		BoundingBox box1 = new BoundingBox(new Location(-1.0, -1.0), new Location(1.0, 1.0));
		BoundingBox box2 = new BoundingBox(new Location(-1.0, -1.0), new Location(1.0, 1.0));
		assertThat(box1, equalTo(box2));
		assertThat(box2, equalTo(box1));
		assertThat(box1.hashCode(), equalTo(box2.hashCode()));
		
		BoundingBox box3 = new BoundingBox(new Location(0.0, -1.0), new Location(1.0, 1.0));
		assertThat(box1, not(equalTo(box3)));
		assertThat(box3, not(equalTo(box1)));
		
		BoundingBox box4 = new BoundingBox(new Location(-1.0, 0.0), new Location(1.0, 1.0));
		assertThat(box1, not(equalTo(box4)));
		assertThat(box4, not(equalTo(box1)));
		
		BoundingBox box5 = new BoundingBox(new Location(-1.0, -1.0), new Location(0.0, 1.0));
		assertThat(box1, not(equalTo(box5)));
		assertThat(box5, not(equalTo(box1)));
		
		BoundingBox box6 = new BoundingBox(new Location(-1.0, -1.0), new Location(1.0, 0.0));
		assertThat(box1, not(equalTo(box6)));
		assertThat(box6, not(equalTo(box1)));
	}
		
//	@Test
//	public void testGetCameraComparator() {
//		{
//			BoundingBox box = new BoundingBox(new Location(0.0, 0.0), new Location(1.0, 0.0));
//			assertThat(box.getCameraComparator(), sameInstance(Cameras.BY_LONGITUDE));
//		}
//		{
//			BoundingBox box = new BoundingBox(new Location(0.0, 0.0), new Location(0.0, 1.0));
//			assertThat(box.getCameraComparator(), sameInstance(Cameras.BY_LATITUDE));
//		}
//		{
//			BoundingBox box = new BoundingBox(new Location(0.0, 1.0), new Location(0.0, 1.0));
//			// 1 degree in each direction, but degree of latitude is consistent,
//			// where degree of longitude is less distance except at the equator
//			assertThat(box.getCameraComparator(), sameInstance(Cameras.BY_LONGITUDE));
//		}
//	}
//	
}