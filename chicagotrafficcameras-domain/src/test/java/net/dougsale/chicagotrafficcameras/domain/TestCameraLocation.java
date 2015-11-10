package net.dougsale.chicagotrafficcameras.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestCameraLocation {

	double latitude = -68.132412;
	double longitude = 104.345612;
	Approach[] approaches = new Approach[] { Approach.EASTBOUND, Approach.SOUTHBOUND };

	@Test
	public void testCameraLocationBasic() {
		CameraLocation location = new CameraLocation(latitude, longitude, approaches);
		assertThat(latitude, equalTo(location.getLatitude()));
		assertThat(longitude, equalTo(location.getLongitude()));
		assertThat(approaches, equalTo(location.getApproaches()));
	}

	@Test
	public void testCameraLocationImmutable() {
		CameraLocation location = new CameraLocation(latitude, longitude, approaches);
		Approach[] copyOfApproaches = location.getApproaches();
		// mutate copy
		copyOfApproaches[0] = Approach.NORTHBOUND;
		// verify that location is unaffected
		assertThat(approaches, equalTo(location.getApproaches()));		
	}
	
	//TODO test out of range lat,long
	@Test(expected=IllegalArgumentException.class)
	public void testCameraLatitudeTooLow() {
		new CameraLocation(-90.000001, longitude, approaches);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCameraLatitudeTooHigh() {
		new CameraLocation(90.000001, longitude, approaches);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCameraLongitudeTooLow() {
		new CameraLocation(latitude, -180.000001, approaches);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCameraLongitudeTooHigh() {
		new CameraLocation(latitude, 180.000001, approaches);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCameraNoApproach() {
		new CameraLocation(latitude, longitude); 
	}

	@Test(expected=NullPointerException.class)
	@SuppressWarnings("all") // want to test var-args invocation with null
	public void testCameraNullApproach() {
		new CameraLocation(latitude, longitude, null); 
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCameraEmptyApproach() {
		new CameraLocation(latitude, longitude, new Approach[0]); 
	}

	@Test(expected=NullPointerException.class)
	public void testCameraNullApproachComponent() {
		new CameraLocation(latitude, longitude, new Approach[] { null }); 
	}
	
	@Test
	public void testEqualsAndHashCode() {
		CameraLocation location1 = new CameraLocation(latitude, longitude, approaches);
		CameraLocation location2 = new CameraLocation(latitude, longitude, approaches);
		assertThat(location1, equalTo(location2));
		assertThat(location2, equalTo(location1));
		assertThat(location1.hashCode(), equalTo(location2.hashCode()));
		
		CameraLocation location3 = new CameraLocation(0f, longitude, approaches);
		assertThat(location1, not(equalTo(location3)));
		assertThat(location3, not(equalTo(location1)));
		
		CameraLocation location4 = new CameraLocation(latitude, 0f, approaches);
		assertThat(location1, not(equalTo(location4)));
		assertThat(location4, not(equalTo(location1)));
		
		CameraLocation location5 = new CameraLocation(latitude, longitude, new Approach[] { Approach.WESTBOUND });
		assertThat(location1, not(equalTo(location5)));
		assertThat(location5, not(equalTo(location1)));
	}
}
