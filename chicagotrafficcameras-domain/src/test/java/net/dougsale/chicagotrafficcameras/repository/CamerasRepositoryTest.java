package net.dougsale.chicagotrafficcameras.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.Cameras;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

public class CamerasRepositoryTest {

	@Test(expected=NullPointerException.class)
	public void testCamerasRepositoryNullResource() {
		new CamerasRepository(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCamerasRepositoryEmptyResource() {
		new CamerasRepository("  ");
	}

	@Test(expected=IOException.class)
	public void testGetCamerasMissingResource() throws ClassNotFoundException, IOException {
		new CamerasRepository("Missing.ser").getCameras();
	}
	
	@Test
	public void testGetCameras() throws ClassNotFoundException, IOException {
		CamerasRepository repo = new CamerasRepository("Cameras.ser");
		Cameras cameras = repo.getCameras();
		assertThat(cameras.get(RedLightCamera.class).size(), equalTo(149));
		assertThat(cameras.get(SpeedCamera.class).size(), equalTo(146));
	}
}
