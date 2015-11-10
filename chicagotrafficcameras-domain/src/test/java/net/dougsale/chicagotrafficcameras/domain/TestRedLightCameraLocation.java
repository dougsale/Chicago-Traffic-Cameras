package net.dougsale.chicagotrafficcameras.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestRedLightCameraLocation {

	String[] intersection = new String[] { "Milwaukee", "North", "Damen" };
	double latitude = -68.132412f;
	double longitude = 104.345612f;
	Approach[] approaches = new Approach[] { Approach.EASTBOUND, Approach.SOUTHBOUND };

	@Test
	public void testRedLightCameraLocationBasic() {
		RedLightCameraLocation location = new RedLightCameraLocation(intersection, latitude, longitude, approaches);
		assertThat(intersection, equalTo(location.getIntersection()));
		assertThat(latitude, equalTo(location.getLatitude()));
		assertThat(longitude, equalTo(location.getLongitude()));
		assertThat(approaches, equalTo(location.getApproaches()));
	}

	@Test
	public void testRedLightCameraLocationImmutable() {
		RedLightCameraLocation location = new RedLightCameraLocation(intersection, latitude, longitude, approaches);

		Approach[] copyOfApproaches = location.getApproaches();
		// mutate copy
		copyOfApproaches[0] = Approach.NORTHBOUND;
		// verify that location is unaffected
		assertThat(approaches, equalTo(location.getApproaches()));		

		String[] copyOfIntersection = location.getIntersection();
		// mutate copy
		copyOfIntersection[0] = "Clark";
		// verify that location is unaffected
		assertThat(approaches, equalTo(location.getApproaches()));		
	}
	
	@Test(expected=NullPointerException.class)
	public void testRedLightCameraNullIntersection() {
		new RedLightCameraLocation(null, latitude, longitude, approaches); 
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRedLightCameraEmptyIntersection() {
		new RedLightCameraLocation(new String[0], latitude, longitude, approaches); 
	}

	@Test(expected=NullPointerException.class)
	public void testRedLightCameraNullIntersectionComponent() {
		new RedLightCameraLocation(new String[] { null }, latitude, longitude, approaches); 
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRedLightCameraEmptyIntersectionComponent() {
		new RedLightCameraLocation(new String[] { "" }, latitude, longitude, approaches); 
	}

	@Test
	public void testEqualsAndHashCode() {
		RedLightCameraLocation location1 = new RedLightCameraLocation(intersection, latitude, longitude, approaches);
		RedLightCameraLocation location2 = new RedLightCameraLocation(intersection, latitude, longitude, approaches);
		assertThat(location1, equalTo(location2));
		assertThat(location2, equalTo(location1));
		assertThat(location1.hashCode(), equalTo(location2.hashCode()));
		
		RedLightCameraLocation location3 = new RedLightCameraLocation(intersection, 0f, longitude, approaches);
		assertThat(location1, not(equalTo(location3)));
		assertThat(location3, not(equalTo(location1)));
		
		RedLightCameraLocation location4 = new RedLightCameraLocation(intersection, latitude, 0f, approaches);
		assertThat(location1, not(equalTo(location4)));
		assertThat(location4, not(equalTo(location1)));
		
		RedLightCameraLocation location5 = new RedLightCameraLocation(intersection, latitude, longitude, new Approach[] { Approach.WESTBOUND });
		assertThat(location1, not(equalTo(location5)));
		assertThat(location5, not(equalTo(location1)));
		
		RedLightCameraLocation location6 = new RedLightCameraLocation(new String[] { "Western", "Armitage" }, latitude, longitude, approaches);
		assertThat(location1, not(equalTo(location6)));
		assertThat(location6, not(equalTo(location1)));
	}
}
