/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

public class CamerasRepositoryTest {

	@Test
	public void testCamerasRepository() {
		String resourceName = "resourceName";
		CamerasRepository repo = new CamerasRepository(resourceName);
		assertThat(repo.getResourceName(), equalTo(resourceName));
	}

	@Test(expected=NullPointerException.class)
	public void testCamerasRepositoryNullResource() {
		new CamerasRepository(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCamerasRepositoryEmptyResource() {
		new CamerasRepository("  ");
	}
	
	//TODO
	// might want to consider moving some stuff around...
	// dependencies between this module and etl...
	// not sure what the answer is
	//TODO
	// how to test init/getCameras interaction
	
	@Test
	public void testGetCameras() throws RepositoryException {
		CamerasRepository repo = new CamerasRepository("Cameras.ser");
		Cameras cameras = repo.getCameras();
		assertThat(cameras.get(RedLightCamera.class).size(), equalTo(149));
		assertThat(cameras.get(SpeedCamera.class).size(), equalTo(146));
	}

	@Test(expected=RepositoryException.class)
	public void testGetCamerasNoSuchResource() throws RepositoryException {
		new CamerasRepository("NoSuchResource.ser").getCameras();
	}
	
}
