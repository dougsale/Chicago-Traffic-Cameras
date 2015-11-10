package net.dougsale.chicagotrafficcameras.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestSpeedCameraLocation {

	String address = "123 Milky Way";
	double latitude = -68.132412f;
	double longitude = 104.345612f;
	Approach[] approaches = new Approach[] { Approach.EASTBOUND, Approach.SOUTHBOUND };

	@Test
	public void testSpeedCameraLocationBasic() {
		SpeedCameraLocation location = new SpeedCameraLocation(address, latitude, longitude, approaches);
		assertThat(address, equalTo(location.getAddress()));
		assertThat(latitude, equalTo(location.getLatitude()));
		assertThat(longitude, equalTo(location.getLongitude()));
		assertThat(approaches, equalTo(location.getApproaches()));
	}

	@Test
	public void testSpeedCameraLocationImmutable() {
		SpeedCameraLocation location = new SpeedCameraLocation(address, latitude, longitude, approaches);
		Approach[] copyOfApproaches = location.getApproaches();
		// mutate copy
		copyOfApproaches[0] = Approach.NORTHBOUND;
		// verify that location is unaffected
		assertThat(approaches, equalTo(location.getApproaches()));		
	}
	
	@Test(expected=NullPointerException.class)
	public void testSpeedCameraNullAddress() {
		new SpeedCameraLocation(null, latitude, longitude, approaches); 
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSpeedCameraEmptyAddress() {
		new SpeedCameraLocation("", latitude, longitude, approaches); 
	}

	@Test
	public void testEqualsAndHashCode() {
		SpeedCameraLocation location1 = new SpeedCameraLocation(address, latitude, longitude, approaches);
		SpeedCameraLocation location2 = new SpeedCameraLocation(address, latitude, longitude, approaches);
		assertThat(location1, equalTo(location2));
		assertThat(location2, equalTo(location1));
		assertThat(location1.hashCode(), equalTo(location2.hashCode()));
		
		SpeedCameraLocation location3 = new SpeedCameraLocation(address, 0f, longitude, approaches);
		assertThat(location1, not(equalTo(location3)));
		assertThat(location3, not(equalTo(location1)));
		
		SpeedCameraLocation location4 = new SpeedCameraLocation(address, latitude, 0f, approaches);
		assertThat(location1, not(equalTo(location4)));
		assertThat(location4, not(equalTo(location1)));
		
		SpeedCameraLocation location5 = new SpeedCameraLocation(address, latitude, longitude, new Approach[] { Approach.WESTBOUND });
		assertThat(location1, not(equalTo(location5)));
		assertThat(location5, not(equalTo(location1)));
		
		SpeedCameraLocation location6 = new SpeedCameraLocation("0 Zed Lane", latitude, longitude, approaches);
		assertThat(location1, not(equalTo(location6)));
		assertThat(location6, not(equalTo(location1)));
	}
}
