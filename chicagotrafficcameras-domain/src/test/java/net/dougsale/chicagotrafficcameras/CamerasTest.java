package net.dougsale.chicagotrafficcameras;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Approach;
import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

public class CamerasTest {

	Location location = new Location(0, 0);
	RedLightCamera rlc = new RedLightCamera(new HashSet<>(Arrays.asList("A St", "B Pl")), location, EnumSet.of(Approach.EASTBOUND));
	SpeedCamera spc = new SpeedCamera("123 A St", location, EnumSet.of(Approach.EASTBOUND));	
	Set<Class<? extends Camera>> types = new HashSet<>(Arrays.asList(RedLightCamera.class, SpeedCamera.class));

	@Test
	public void testCameras() {
		Cameras cameras = new Cameras();
		
		cameras.add(rlc);
		assertThat(cameras.getTypes(), equalTo(Collections.singleton(rlc.getClass())));
		assertThat(cameras.get(rlc.getClass()), equalTo(Collections.singleton(rlc)));
		
		cameras.add(spc);
		assertThat(cameras.getTypes(), equalTo(types));
		assertThat(cameras.get(spc.getClass()), equalTo(Collections.singleton(spc)));
		
		assertThat(cameras.get(Camera.class), equalTo(Collections.emptySet()));
	}
	
	@Test(expected=NullPointerException.class)
	public void testAddNullCamera() {
		Cameras cameras = new Cameras();
		cameras.add(null);
	}

	@Test
	public void testCamerasSorted() {
		RedLightCamera rlCamera_1 = new RedLightCamera(
				new TreeSet<String>(Arrays.asList("Milwaukee", "North", "Damen")),
				new Location(-68.132412, 104.345612),
				EnumSet.of(Approach.EASTBOUND, Approach.SOUTHBOUND)
			);
		
		RedLightCamera rlCamera_2 = new RedLightCamera(
				new TreeSet<String>(Arrays.asList("Western", "North")),
				new Location(-67.156712, 103.312312),
				EnumSet.of(Approach.NORTHBOUND, Approach.SOUTHBOUND)
			);
		
		SpeedCamera spCamera_1 = new SpeedCamera(
				"3312 W Fullerton Ave",
				new Location(-68.132412, 104.345612),
				EnumSet.of(Approach.EASTBOUND, Approach.WESTBOUND)
			);
		
		SpeedCamera spCamera_2 = new SpeedCamera(
				"6123 N Cicero Ave",
				new Location(-67.156712, 103.312312),
				EnumSet.of(Approach.NORTHBOUND, Approach.SOUTHBOUND)
			);
		
		Cameras cameras = new Cameras();
		cameras.add(rlCamera_1);
		cameras.add(rlCamera_2);
		cameras.add(spCamera_1);
		cameras.add(spCamera_2);
		
		Iterator<RedLightCamera> rlcIter;
		rlcIter = cameras.get(RedLightCamera.class, Cameras.Sorted.BY_LATITUDE).iterator();
		assertThat(rlcIter.next(), equalTo(rlCamera_1));
		assertThat(rlcIter.next(), equalTo(rlCamera_2));
		rlcIter = cameras.get(RedLightCamera.class, Cameras.Sorted.BY_LONGITUDE).iterator();
		assertThat(rlcIter.next(), equalTo(rlCamera_2));
		assertThat(rlcIter.next(), equalTo(rlCamera_1));
		rlcIter = cameras.get(RedLightCamera.class, Cameras.Sorted.BY_LATITUDE_DECREASING).iterator();
		assertThat(rlcIter.next(), equalTo(rlCamera_2));
		assertThat(rlcIter.next(), equalTo(rlCamera_1));
		rlcIter = cameras.get(RedLightCamera.class, Cameras.Sorted.BY_LONGITUDE_DECREASING).iterator();
		assertThat(rlcIter.next(), equalTo(rlCamera_1));
		assertThat(rlcIter.next(), equalTo(rlCamera_2));

		Iterator<SpeedCamera> spcIter;
		spcIter = cameras.get(SpeedCamera.class, Cameras.Sorted.BY_LATITUDE).iterator();
		assertThat(spcIter.next(), equalTo(spCamera_1));
		assertThat(spcIter.next(), equalTo(spCamera_2));
		spcIter = cameras.get(SpeedCamera.class, Cameras.Sorted.BY_LONGITUDE).iterator();
		assertThat(spcIter.next(), equalTo(spCamera_2));
		assertThat(spcIter.next(), equalTo(spCamera_1));
		spcIter = cameras.get(SpeedCamera.class, Cameras.Sorted.BY_LATITUDE_DECREASING).iterator();
		assertThat(spcIter.next(), equalTo(spCamera_2));
		assertThat(spcIter.next(), equalTo(spCamera_1));
		spcIter = cameras.get(SpeedCamera.class, Cameras.Sorted.BY_LONGITUDE_DECREASING).iterator();
		assertThat(spcIter.next(), equalTo(spCamera_1));
		assertThat(spcIter.next(), equalTo(spCamera_2));
	}
}
