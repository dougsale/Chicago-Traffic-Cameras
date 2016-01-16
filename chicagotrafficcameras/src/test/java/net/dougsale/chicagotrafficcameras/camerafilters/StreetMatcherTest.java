/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

public class StreetMatcherTest {

	@Test
	public void testStreetMatcher() {
		String street = " stREet	";
		StreetMatcher matcher = new StreetMatcher(street);
		assertThat(matcher.getStreet(), equalTo(street.trim().toUpperCase()));
	}
	
	@Test(expected=NullPointerException.class)
	public void testStreetMatcherNullStreet() {
		new StreetMatcher(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testStreetMatcherEmptyStreet() {
		new StreetMatcher(" ");
	}
	
	@Test
	public void testStreetMatcherProtectedConstructor() {
		StreetMatcher matcher = new StreetMatcher() {};
		assertThat(matcher.getStreet(), nullValue());
	}

	@Test
	public void testAccept() {
		Camera camera;
		
		camera = when(mock(RedLightCamera.class).getIntersection()).thenReturn(new HashSet<>(Arrays.asList("Madison", "Ashland"))).getMock();
		assertThat(new StreetMatcher("N Ashland Ave").accept(camera), is(true));
		
		camera = when(mock(SpeedCamera.class).getAddress()).thenReturn("123 Ashland Ave").getMock();
		assertThat(new StreetMatcher("N Ashland Ave").accept(camera), is(true));
		
		camera = when(mock(RedLightCamera.class).getIntersection()).thenReturn(new HashSet<String>(Arrays.asList("Madison", "Ashland"))).getMock();
		assertThat(new StreetMatcher("N Ashland Ave").accept(camera), is(true));
	
		camera = when(mock(SpeedCamera.class).getAddress()).thenReturn("123 Ashland Ave").getMock();
		assertThat(new StreetMatcher("N Ashland Ave").accept(camera), is(true));

		camera = when(mock(RedLightCamera.class).getIntersection()).thenReturn(new HashSet<>(Arrays.asList("Madison", "Western"))).getMock();
		assertThat(new StreetMatcher("N Ashland Ave").accept(camera), is(false));
	
		camera = when(mock(SpeedCamera.class).getAddress()).thenReturn("123 Madison Ave").getMock();
		assertThat(new StreetMatcher("N Ashland Ave").accept(camera), is(false));
	}
	
	@Test
	public void testStreetForAddress() {
		StreetMatcher matcher = new StreetMatcher("foo");	
		assertThat(matcher.streetForAddress("123 Ashland Ave "), equalTo("Ashland Ave"));
		assertThat(matcher.streetForAddress("2448 N Clybourn Ave "), equalTo("Clybourn Ave"));
		assertThat(matcher.streetForAddress(" 115 N Ogden"), equalTo("Ogden"));
		assertThat(matcher.streetForAddress("3130 N  Ashland Ave"), equalTo("Ashland Ave"));
		assertThat(matcher.streetForAddress("6510 W Bryn  Mawr Ave"), equalTo("Bryn Mawr Ave"));
		assertThat(matcher.streetForAddress("10318 S  Indianapolis"), equalTo("Indianapolis"));
		assertThat(matcher.streetForAddress("3230  N Milwaukee Ave"), equalTo("Milwaukee Ave"));
		assertThat(matcher.streetForAddress(" 2912  W  Roosevelt  Rd  "), equalTo("Roosevelt Rd"));
		assertThat(matcher.streetForAddress("3655 W Jackson Blvd"), equalTo("Jackson Blvd"));
	}	

	@Test
	public void testEqualsHashCode() {
		StreetMatcher filter1 = new StreetMatcher("STREET");
		StreetMatcher filter2 = new StreetMatcher("street");
		assertThat(filter1, equalTo(filter2));
		assertThat(filter2, equalTo(filter1));
		assertThat(filter1.hashCode(), equalTo(filter2.hashCode()));
		
		StreetMatcher filter3 = new StreetMatcher("  Street");
		assertThat(filter1, equalTo(filter3));
		assertThat(filter3, equalTo(filter1));
		assertThat(filter1.hashCode(), equalTo(filter3.hashCode()));
		
		StreetMatcher filter4 = new StreetMatcher("N Street");
		assertThat(filter1, not(equalTo(filter4)));
		assertThat(filter4, not(equalTo(filter1)));
		
		StreetMatcher filter5 = new StreetMatcher("Street Way");
		assertThat(filter1, not(equalTo(filter5)));
		assertThat(filter5, not(equalTo(filter1)));
	}

}