package net.dougsale.chicagotrafficcameras.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.CamerasFactory;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;
import net.dougsale.chicagotrafficcameras.repository.CamerasRepository;
import net.dougsale.chicagotrafficcameras.repository.RepositoryException;

public class CamerasFactoryTest {

	@Test
	public void testCamerasFactory() {
		CamerasRepository repository = mock(CamerasRepository.class);
		CamerasFactory factory = new CamerasFactory(repository);
		assertThat(factory.getRepository(), sameInstance(repository));
	}

	@Test(expected=NullPointerException.class)
	public void testCamerasFactoryNullCamerasRepository() {
		new CamerasFactory(null);
	}
	
	@Test
	public void testGetEmptyCameras() {
		CamerasRepository repository = mock(CamerasRepository.class);
		CamerasFactory factory = new CamerasFactory(repository);
		Cameras cameras = factory.getEmptyCameras();
		assertThat(cameras, not(nullValue()));
		assertThat(cameras.size(), equalTo(0));
	}

	@Test
	public void testGetAllCameras() throws RepositoryException {
		Cameras cameras = new Cameras();
		cameras.add(mock(Camera.class));
		cameras.add(mock(SpeedCamera.class));
		cameras.add(mock(RedLightCamera.class));
		
		CamerasRepository repository = mock(CamerasRepository.class);
		when(repository.getCameras()).thenReturn(cameras);
		
		CamerasFactory factory = new CamerasFactory(repository);
		
		Cameras result = factory.getAllCameras();
		assertThat(result, not(nullValue()));
		assertThat(result, equalTo(cameras));		
		verify(repository, times(1)).getCameras();
		
		assertThat(result, not(nullValue()));
		assertThat(result, equalTo(cameras));		
		verify(repository, times(1)).getCameras(); // 2 calls to factory.getAllCameras(), only 1 call to repository.getCameras()
	}

}
