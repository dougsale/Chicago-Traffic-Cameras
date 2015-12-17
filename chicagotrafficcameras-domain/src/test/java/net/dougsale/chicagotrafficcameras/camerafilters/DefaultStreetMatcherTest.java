package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Approach;
import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

public class DefaultStreetMatcherTest {

	@Test(expected=NullPointerException.class)
	public void testDefaultStreetMatcherNullStreet() {
		new DefaultStreetMatcher(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testDefaultStreetMatcherEmptyStreet() {
		new DefaultStreetMatcher("");
	}

	@Test
	public void testAccept() {
		{
			RedLightCamera rlCamera = new RedLightCamera(
				new HashSet<String>(Arrays.asList("Madison", "Ashland")), mock(Location.class), EnumSet.of(Approach.EASTBOUND));
			assertThat(new DefaultStreetMatcher("N Ashland Ave").accept(rlCamera), is(true));
	
			SpeedCamera spCamera = new SpeedCamera(
				"123 Ashland Ave", mock(Location.class), EnumSet.of(Approach.EASTBOUND));
			assertThat(new DefaultStreetMatcher("N Ashland Ave").accept(spCamera), is(true));
		}
		{
			RedLightCamera rlCamera = new RedLightCamera(
				new HashSet<String>(Arrays.asList("Madison", "Western")), mock(Location.class), EnumSet.of(Approach.EASTBOUND));
			assertThat(new DefaultStreetMatcher("N Ashland Ave").accept(rlCamera), is(false));
	
			SpeedCamera spCamera = new SpeedCamera(
				"123 Madison Ave", mock(Location.class), EnumSet.of(Approach.EASTBOUND));
			assertThat(new DefaultStreetMatcher("N Ashland Ave").accept(spCamera), is(false));
		}
	}

	@Test
	public void testMatch() {
		{
			RedLightCamera rlCamera = new RedLightCamera(
				new HashSet<String>(Arrays.asList("Madison", "Ashland")), mock(Location.class), EnumSet.of(Approach.EASTBOUND));
			assertThat(new DefaultStreetMatcher("N Ashland Ave").match(rlCamera), is(true));
	
			SpeedCamera spCamera = new SpeedCamera(
				"123 Ashland Ave", mock(Location.class), EnumSet.of(Approach.EASTBOUND));
			assertThat(new DefaultStreetMatcher("N Ashland Ave").match(spCamera), is(true));
		}
		{
			RedLightCamera rlCamera = new RedLightCamera(
				new HashSet<String>(Arrays.asList("Madison", "Western")), mock(Location.class), EnumSet.of(Approach.EASTBOUND));
			assertThat(new DefaultStreetMatcher("N Ashland Ave").match(rlCamera), is(false));
	
			SpeedCamera spCamera = new SpeedCamera(
				"123 Madison Ave", mock(Location.class), EnumSet.of(Approach.EASTBOUND));
			assertThat(new DefaultStreetMatcher("N Ashland Ave").match(spCamera), is(false));
		}
	}

	@Test
	public void testStreetForAddress() {
		DefaultStreetMatcher matcher = new DefaultStreetMatcher("foo");	
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
}