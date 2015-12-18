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
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class SpeedCameraTest {

	String address = "123 Milky Way";
	double latitude = -68.132412f;
	double longitude = 104.345612f;
	Location location = new Location(latitude, longitude);
	Set<Approach> approaches = EnumSet.of(Approach.EASTBOUND, Approach.SOUTHBOUND);

	// constructor 
	
	@Test
	public void testSpeedCameraBasic() {
		SpeedCamera camera = new SpeedCamera(address, location, approaches);
		assertThat(address, equalTo(camera.address));
		assertThat(latitude, equalTo(camera.location.latitude));
		assertThat(longitude, equalTo(camera.location.longitude));
		assertThat(approaches, equalTo(camera.approaches));
	}

	// immutability 
	// speed camera fields are marked final;  location's member fields are final
	// their immutability handled by the compiler
	
	@Test
	public void testRedLightCameraImmutable() {
		Set<Approach> approaches = new HashSet<Approach>(this.approaches);
		
		SpeedCamera camera = new SpeedCamera(address, location, approaches);

		approaches.clear(); // clear the provided set - should be copied
		
		assertThat(approaches, not(equalTo(camera.approaches)));
		assertThat(this.approaches, equalTo(camera.approaches));
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testRedLightCameraIntersectionImmutable() {
		// approaches set is unmodifiable
		new SpeedCamera(address, location, approaches).approaches.clear();
	}
	
	// validate constructor parameters
	
	@Test(expected=NullPointerException.class)
	public void testSpeedCameraNullAddress() {
		new SpeedCamera(null, location, approaches); 
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSpeedCameraEmptyAddress() {
		new SpeedCamera("", location, approaches); 
	}

	@Test(expected=NullPointerException.class)
	public void testSpeedCameraNullLocation() {
		new SpeedCamera(address, null, approaches); 
	}

	@Test(expected=NullPointerException.class)
	public void testSpeedCameraNullApproaches() {
		new SpeedCamera(address, location, null); 
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSpeedCameraEmptyApproaches() {
		new SpeedCamera(address, location, Collections.emptySet()); 
	}

	@Test(expected=NullPointerException.class)
	public void testSpeedCameraNullApproachesComponent() {
		new SpeedCamera(address, location, Collections.singleton(null)); 
	}

	@Test
	public void testEqualsAndHashCode() {
		SpeedCamera camera1 = new SpeedCamera(address, location, approaches);
		SpeedCamera camera2 = new SpeedCamera(address, location, approaches);
		assertThat(camera1, equalTo(camera2));
		assertThat(camera2, equalTo(camera1));
		assertThat(camera1.hashCode(), equalTo(camera2.hashCode()));
		
		SpeedCamera camera3 = new SpeedCamera(address, new Location(0f, longitude), approaches);
		assertThat(camera1, not(equalTo(camera3)));
		assertThat(camera3, not(equalTo(camera1)));
		
		SpeedCamera camera4 = new SpeedCamera(address, new Location(latitude, 0f), approaches);
		assertThat(camera1, not(equalTo(camera4)));
		assertThat(camera4, not(equalTo(camera1)));
		
		SpeedCamera camera5 = new SpeedCamera(address, location, Collections.singleton(Approach.WESTBOUND));
		assertThat(camera1, not(equalTo(camera5)));
		assertThat(camera5, not(equalTo(camera1)));
		
		SpeedCamera camera6 = new SpeedCamera("0 Zed Lane", location, approaches);
		assertThat(camera1, not(equalTo(camera6)));
		assertThat(camera6, not(equalTo(camera1)));
	}
}
