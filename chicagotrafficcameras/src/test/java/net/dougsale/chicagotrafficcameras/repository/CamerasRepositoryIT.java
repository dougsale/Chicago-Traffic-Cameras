/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/app-context.xml")
public class CamerasRepositoryIT {

	@Autowired
	CamerasRepository camerasRepository;

	@Test
	public void testCamerasRepository() throws RepositoryException {		
		Cameras cameras = camerasRepository.getCameras();		
		assertThat(cameras.size(), equalTo(295));
		assertThat(cameras.get(RedLightCamera.class).size(), equalTo(149));
		assertThat(cameras.get(SpeedCamera.class).size(), equalTo(146));
	}
}
