/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.junit.Test;

public class CameraTest {

	double latitude = -68.132412;
	double longitude = 104.345612;
	Location location = new Location(latitude, longitude);
	Set<Approach> approaches = EnumSet.of(Approach.EASTBOUND, Approach.SOUTHBOUND);

	@Test
	public void testCameraBasic() {
		Camera camera = new Camera(location, approaches);
		assertThat(latitude, equalTo(camera.location.latitude));
		assertThat(longitude, equalTo(camera.location.longitude));
		assertThat(approaches, equalTo(camera.approaches));
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testCameraImmutable() {
		// location is final, attempts to change caught by compiler
		Camera camera = new Camera(location, approaches);
		camera.approaches.add(Approach.NORTHBOUND);
	}

	@Test(expected=NullPointerException.class)
	public void testCameraNullLocation() {
		new Camera(null, approaches); 
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCameraEmptyApproach() {
		new Camera(location, Collections.emptySet()); 
	}

	@Test(expected=NullPointerException.class)
	public void testCameraNullApproachComponent() {
		new Camera(location, Collections.singleton(null)); 
	}
	
	@Test
	public void testEqualsAndHashCode() {
		Camera camera1 = new Camera(location, approaches);
		Camera camera2 = new Camera(location, approaches);
		assertThat(camera1, equalTo(camera2));
		assertThat(camera2, equalTo(camera1));
		assertThat(camera1.hashCode(), equalTo(camera2.hashCode()));
		
		Camera camera3 = new Camera(new Location(0f, longitude), approaches);
		assertThat(camera1, not(equalTo(camera3)));
		assertThat(camera3, not(equalTo(camera1)));
		
		Camera camera4 = new Camera(new Location(latitude, 0f), approaches);
		assertThat(camera1, not(equalTo(camera4)));
		assertThat(camera4, not(equalTo(camera1)));
		
		Camera camera5 = new Camera(location, Collections.singleton(Approach.WESTBOUND));
		assertThat(camera1, not(equalTo(camera5)));
		assertThat(camera5, not(equalTo(camera1)));
	}
}
