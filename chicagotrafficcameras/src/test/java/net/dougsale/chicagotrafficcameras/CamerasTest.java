/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Approach;
import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

public class CamerasTest {

	@Test
	public void testAdd_GetTypes_GetByType_Get() {
		Cameras cameras = new Cameras();
		RedLightCamera rlc = mock(RedLightCamera.class);
		SpeedCamera spc = mock(SpeedCamera.class);	
		
		cameras.add(rlc);
		assertThat(cameras.getTypes(), equalTo(Collections.singleton(rlc.getClass())));
		assertThat(cameras.get(rlc.getClass()), equalTo(Collections.singleton(rlc)));
		
		cameras.add(spc);
		assertThat(cameras.getTypes(), equalTo(
				new HashSet<Class<? extends Camera>>(Arrays.asList(rlc.getClass(), spc.getClass()))));
		assertThat(cameras.get(spc.getClass()), equalTo(Collections.singleton(spc)));
		
		assertThat(cameras.get(Camera.class), equalTo(Collections.emptySet()));
		
		assertThat(cameras.get(), equalTo(
				new HashSet<Camera>(Arrays.asList(rlc, spc))));
	}
	
	@Test
	public void testAdd_GetByTypeSorted_GetSorted() {

		RedLightCamera rlc1 = new RedLightCamera(
				new TreeSet<String>(Arrays.asList("Milwaukee", "North", "Damen")),
				new Location(-68.132412, 104.345612),
				EnumSet.of(Approach.EASTBOUND, Approach.SOUTHBOUND)
			);
		
		RedLightCamera rlc2 = new RedLightCamera(
				new TreeSet<String>(Arrays.asList("Western", "North")),
				new Location(-67.156712, 103.312312),
				EnumSet.of(Approach.NORTHBOUND, Approach.SOUTHBOUND)
			);
		
		SpeedCamera spc1 = new SpeedCamera(
				"3312 W Fullerton Ave",
				new Location(-70.132412, 105.345612),
				EnumSet.of(Approach.EASTBOUND, Approach.WESTBOUND)
			);
		
		SpeedCamera spc2 = new SpeedCamera(
				"6123 N Cicero Ave",
				new Location(-66.156712, 102.312312),
				EnumSet.of(Approach.NORTHBOUND, Approach.SOUTHBOUND)
			);
		
		Cameras cameras = new Cameras();
		cameras.add(rlc1);
		cameras.add(rlc2);
		cameras.add(spc1);
		cameras.add(spc2);
		
		Iterator<RedLightCamera> rlcIter;
		rlcIter = cameras.get(RedLightCamera.class, Cameras.BY_LATITUDE).iterator();
		assertThat(rlcIter.next(), equalTo(rlc1));
		assertThat(rlcIter.next(), equalTo(rlc2));
		rlcIter = cameras.get(RedLightCamera.class, Cameras.BY_LONGITUDE).iterator();
		assertThat(rlcIter.next(), equalTo(rlc2));
		assertThat(rlcIter.next(), equalTo(rlc1));

		Iterator<SpeedCamera> spcIter;
		spcIter = cameras.get(SpeedCamera.class, Cameras.BY_LATITUDE).iterator();
		assertThat(spcIter.next(), equalTo(spc1));
		assertThat(spcIter.next(), equalTo(spc2));
		spcIter = cameras.get(SpeedCamera.class, Cameras.BY_LONGITUDE).iterator();
		assertThat(spcIter.next(), equalTo(spc2));
		assertThat(spcIter.next(), equalTo(spc1));
		
		Iterator<Camera> cIter;
		cIter = cameras.get(Cameras.BY_LATITUDE).iterator();
		assertThat(cIter.next(), equalTo(spc1));
		assertThat(cIter.next(), equalTo(rlc1));
		assertThat(cIter.next(), equalTo(rlc2));
		assertThat(cIter.next(), equalTo(spc2));
		cIter = cameras.get(Cameras.BY_LONGITUDE).iterator();
		assertThat(cIter.next(), equalTo(spc2));
		assertThat(cIter.next(), equalTo(rlc2));
		assertThat(cIter.next(), equalTo(rlc1));
		assertThat(cIter.next(), equalTo(spc1));
	}

	// Test invalid parameters
	
	@Test(expected=NullPointerException.class)
	public void testAddNullCamera() {
		Cameras cameras = new Cameras();
		cameras.add(null);
	}

	@Test(expected=NullPointerException.class)
	public void testGetByTypeNullType() {
		Cameras cameras = new Cameras();
		Class<SpeedCamera> type = null;
		cameras.get(type);
	}

	@Test(expected=NullPointerException.class)
	public void testGetSortedNullComparator() {
		Cameras cameras = new Cameras();
		Comparator<Object> comparator = null;
		cameras.get(comparator);
	}

	@Test(expected=NullPointerException.class)
	public void testGetByTypeSortedNullType() {
		Cameras cameras = new Cameras();
		Class<RedLightCamera> type = null;
		Comparator<Camera> comparator = Cameras.BY_LATITUDE;
		cameras.get(type, comparator);
	}

	@Test(expected=NullPointerException.class)
	public void testGetByTypeSortedNullComparator() {
		Cameras cameras = new Cameras();
		Class<? extends Camera> type = RedLightCamera.class;
		Comparator<Camera> comparator = null;
		cameras.get(type, comparator);
	}

}