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
	public void testRedLightCameraBasic() {
		RedLightCamera camera = new RedLightCamera(intersection, location, approaches);
		assertThat(intersection, equalTo(camera.intersection));
		assertThat(latitude, equalTo(camera.location.latitude));
		assertThat(longitude, equalTo(camera.location.longitude));
		assertThat(approaches, equalTo(camera.approaches));
	}

	// immutability
	
	@Test
	public void testRedLightCameraImmutable() {
		Set<String> intersection = new HashSet<String>(this.intersection);
		Set<Approach> approaches = new HashSet<Approach>(this.approaches);
		
		RedLightCamera camera = new RedLightCamera(intersection, location, approaches);

		intersection.clear();
		approaches.clear();
		
		assertThat(intersection, not(equalTo(camera.intersection)));
		assertThat(approaches, not(equalTo(camera.approaches)));
		assertThat(this.intersection, equalTo(camera.intersection));
		assertThat(this.approaches, equalTo(camera.approaches));
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testRedLightCameraIntersectionImmutable() {
		new RedLightCamera(intersection, location, approaches).intersection.add("Foo Way");
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testRedLightCameraApproachesImmutable() {
		new RedLightCamera(intersection, location, approaches).approaches.clear();
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
