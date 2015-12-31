/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class RedLightCameraTest {

	double latitude = -68.132412f;
	double longitude = 104.345612f;
	Location location = new Location(latitude, longitude);
	Set<Approach> approaches = EnumSet.of(Approach.EASTBOUND, Approach.SOUTHBOUND);
	Set<String> intersection = new HashSet<String>(Arrays.asList("Milwaukee", "North", "Damen"));

	// constructor
	
	@Test
	public void testRedLightCamera() {
		RedLightCamera camera = new RedLightCamera(intersection, location, approaches);
		assertThat(intersection, equalTo(camera.getIntersection()));
		assertThat(location, equalTo(camera.getLocation()));
		assertThat(approaches, equalTo(camera.getApproaches()));
	}

	// Immutability
	
	@Test
	public void testRedLightCameraImmutableIntersectionParameterCopied() {
		Set<String> intersection = new HashSet<>(this.intersection);
		RedLightCamera camera = new RedLightCamera(intersection, location, approaches);
		intersection.clear();
		assertThat(camera.getIntersection(), not(equalTo(intersection)));
		assertThat(camera.getIntersection(), equalTo(this.intersection));
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRedLightCameraImmutableIntersectionUnmodifiable() {
		RedLightCamera camera = new RedLightCamera(intersection, location, approaches);
		camera.getIntersection().clear();
	}

	@Test
	public void testRedLightCameraImmutableApproachesParameterCopied() {
		Set<Approach> approaches = EnumSet.copyOf(this.approaches);
		RedLightCamera camera = new RedLightCamera(intersection, location, approaches);
		approaches.clear();
		assertThat(camera.getApproaches(), not(equalTo(approaches)));
		assertThat(camera.getApproaches(), equalTo(this.approaches));
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRedLightCameraImmutableApproachesUnmodifiable() {
		RedLightCamera camera = new RedLightCamera(intersection, location, approaches);
		camera.getApproaches().add(Approach.NORTHBOUND);
	}

	// argument validation
	
	@Test(expected=NullPointerException.class)
	public void testRedLightCameraNullIntersection() {
		new RedLightCamera(null, location, approaches); 
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRedLightCameraEmptyIntersection() {
		new RedLightCamera(Collections.emptySet(), location, approaches); 
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRedLightCameraSingleIntersectionStreet() {
		new RedLightCamera(Collections.singleton("Foo Way"), location, approaches); 
	}

	@Test(expected=NullPointerException.class)
	public void testRedLightCameraNullIntersectionComponent() {
		new RedLightCamera(new HashSet<String>(Arrays.asList("Foo Way", null)), location, approaches); 
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRedLightCameraEmptyIntersectionStreet() {
		new RedLightCamera(new HashSet<String>(Arrays.asList("", "Foo Way")), location, approaches); 
	}

	@Test(expected=NullPointerException.class)
	public void testSpeedCameraNullLocation() {
		new RedLightCamera(intersection, null, approaches); 
	}

	@Test(expected=NullPointerException.class)
	public void testRedLightCameraNullApproaches() {
		new RedLightCamera(intersection, location, null); 
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRedLightCameraEmptyApproaches() {
		new RedLightCamera(intersection, location, Collections.emptySet()); 
	}

	@Test(expected=NullPointerException.class)
	public void testRedLightCameraNullApproachesComponent() {
		new RedLightCamera(intersection, location, Collections.singleton(null)); 
	}

	@Test
	public void testEqualsAndHashCode() {
		RedLightCamera camera1 = new RedLightCamera(intersection, location, approaches);
		RedLightCamera camera2 = new RedLightCamera(intersection, location, approaches);
		assertThat(camera1, equalTo(camera2));
		assertThat(camera2, equalTo(camera1));
		assertThat(camera1.hashCode(), equalTo(camera2.hashCode()));
		
		RedLightCamera camera3 = new RedLightCamera(intersection, new Location(0f, longitude), approaches);
		assertThat(camera1, not(equalTo(camera3)));
		assertThat(camera3, not(equalTo(camera1)));
		
		RedLightCamera camera4 = new RedLightCamera(intersection, new Location(latitude, 0f), approaches);
		assertThat(camera1, not(equalTo(camera4)));
		assertThat(camera4, not(equalTo(camera1)));
		
		RedLightCamera camera5 = new RedLightCamera(intersection, location, Collections.singleton(Approach.WESTBOUND));
		assertThat(camera1, not(equalTo(camera5)));
		assertThat(camera5, not(equalTo(camera1)));
		
		RedLightCamera camera6 = new RedLightCamera(new HashSet<String>(Arrays.asList("Western", "Armitage")), location, approaches);
		assertThat(camera1, not(equalTo(camera6)));
		assertThat(camera6, not(equalTo(camera1)));
	}
}
