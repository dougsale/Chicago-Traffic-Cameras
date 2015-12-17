package net.dougsale.chicagotrafficcameras.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class LocationTest {

	double latitude = -68.132412;
	double longitude = 104.345612;
	
	@Test
	public void testLocation() {
		Location location = new Location(latitude, longitude);
		assertThat(latitude, equalTo(location.latitude));
		assertThat(longitude, equalTo(location.longitude));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLatitudeTooLow() {
		new Location(-90.000001, longitude);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testLatitudeTooHigh() {
		new Location(90.000001, longitude);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testLongitudeTooLow() {
		new Location(latitude, -180.000001);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testLongitudeTooHigh() {
		new Location(latitude, 180.000001);
	}

	@Test
	public void testEqualsAndHashCode() {
		Location location1 = new Location(latitude, longitude);
		Location location2 = new Location(latitude, longitude);
		assertThat(location1, equalTo(location2));
		assertThat(location2, equalTo(location1));
		assertThat(location1.hashCode(), equalTo(location2.hashCode()));
		
		Location location3 = new Location(0f, longitude);
		assertThat(location1, not(equalTo(location3)));
		assertThat(location3, not(equalTo(location1)));
		
		Location location4 = new Location(latitude, 0f);
		assertThat(location1, not(equalTo(location4)));
		assertThat(location4, not(equalTo(location1)));
	}
}