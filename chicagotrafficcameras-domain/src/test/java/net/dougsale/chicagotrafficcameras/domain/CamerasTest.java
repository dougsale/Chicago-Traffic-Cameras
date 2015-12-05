package net.dougsale.chicagotrafficcameras.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class CamerasTest {

	Location location = new Location(0, 0);

	RedLightCamera rlc = new RedLightCamera(
			new HashSet<>(Arrays.asList("A St", "B Pl")), location, Collections.singleton(Approach.EASTBOUND));

	SpeedCamera spc = new SpeedCamera("123 A St", location, Collections.singleton(Approach.EASTBOUND));
	
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
}
