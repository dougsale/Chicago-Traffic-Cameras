/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Direction;
import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

public class CamerasTest {

	//TODO make other tests like testSize(), testRemove()
	
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
	public void testAdd() {
		Cameras cameras = new Cameras();
		Set<Camera> set = new HashSet<>();
		
		int count = 10;		
		for (int i = 1; i <= count; i++) {
			Camera camera = i % 2 == 0? mock(RedLightCamera.class) : mock(SpeedCamera.class);
			cameras.add(camera);
			set.add(camera);
		}

		assertThat(cameras.size(), equalTo(count));
		assertThat(cameras.get(), equalTo(set));
	}	

	@Test
	public void testAddAll() {
		Cameras source1 = new Cameras();
		Cameras source2 = new Cameras();
		
		Set<Camera> set = new HashSet<>();
		
		Cameras cameras = new Cameras();

		int count = 10;		
		for (int i = 1; i <= count; i++) {
			Camera camera = i % 2 == 0? mock(RedLightCamera.class) : mock(SpeedCamera.class);
			source1.add(camera);
			source2.add(camera);
			set.add(camera);
		}
		
		// add 10 cameras
		cameras.addAll(source1);
		assertThat(cameras.size(), equalTo(count));
		assertThat(cameras.get(), equalTo(set));
		
		// add no cameras
		cameras.addAll(new Cameras());
		assertThat(cameras.size(), equalTo(count));
		assertThat(cameras.get(), equalTo(set));
		
		for (int i = 1; i <= count; i++) {
			Camera camera = i % 2 == 0? mock(RedLightCamera.class) : mock(SpeedCamera.class);
			source2.add(camera);
			set.add(camera);
		}

		// add 20 cameras, 10 of which are duplicates
		cameras.addAll(source2);
		assertThat(cameras.size(), equalTo(2 * count));
		assertThat(cameras.get(), equalTo(set));
	}
	
	@Test(expected=NullPointerException.class)
	public void testAddAllNullCameras() {
		new Cameras().addAll(null);
	}
	
	@Test
	public void testSize() {
		Cameras cameras = new Cameras();
		assertThat(cameras.size(), equalTo(0));

		int count = 10;		
		for (int i = 1; i <= count; i++) {
			Camera camera = i % 2 == 0? mock(RedLightCamera.class) : mock(SpeedCamera.class);
			cameras.add(camera);
			assertThat(cameras.size(), equalTo(i));
		}
	}
	
	@Test
	public void testRemove() {
		Cameras cameras = new Cameras();

		// remove from empty cameras collection
		assertThat(cameras.remove(mock(Camera.class)), is(false));
		
		// add some cameras
		int count = 10;
		Camera camera = null;
		for (int i = 0; i < count; i++) {
			camera = i % 2 == 0? mock(RedLightCamera.class) : mock(SpeedCamera.class);
			cameras.add(camera);
		}
		
		// remove the camera added last
		assertThat(cameras.remove(camera), is(true));
		assertThat(cameras.size(), equalTo(count - 1));
		assertThat(cameras.get().contains(camera), is(false));
		
		// remove a camera not in the set
		assertThat(cameras.remove(mock(Camera.class)), is(false));
	}
	
	@Test(expected=NullPointerException.class)
	public void testRemoveNullCamera() {
		new Cameras().remove(null);
	}
	
	@Test
	public void testAdd_GetByTypeSorted_GetSorted() {

		RedLightCamera rlc1 = new RedLightCamera(
				new TreeSet<String>(Arrays.asList("Milwaukee", "North", "Damen")),
				new Location(-68.132412, 104.345612),
				EnumSet.of(Direction.EASTBOUND, Direction.SOUTHBOUND)
			);
		
		RedLightCamera rlc2 = new RedLightCamera(
				new TreeSet<String>(Arrays.asList("Western", "North")),
				new Location(-67.156712, 103.312312),
				EnumSet.of(Direction.NORTHBOUND, Direction.SOUTHBOUND)
			);
		
		SpeedCamera spc1 = new SpeedCamera(
				"3312 W Fullerton Ave",
				new Location(-70.132412, 105.345612),
				EnumSet.of(Direction.EASTBOUND, Direction.WESTBOUND)
			);
		
		SpeedCamera spc2 = new SpeedCamera(
				"6123 N Cicero Ave",
				new Location(-66.156712, 102.312312),
				EnumSet.of(Direction.NORTHBOUND, Direction.SOUTHBOUND)
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