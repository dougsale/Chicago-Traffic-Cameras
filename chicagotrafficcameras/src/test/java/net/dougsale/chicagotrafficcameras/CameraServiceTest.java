package net.dougsale.chicagotrafficcameras;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.repository.CamerasRepository;
import net.dougsale.chicagotrafficcameras.repository.RepositoryException;

public class CameraServiceTest {

	@Test
	public void testCameraService() {
		CamerasRepository repo = mock(CamerasRepository.class);
		CameraLocator locator = mock(CameraLocator.class);
		
		CameraService service = new CameraService(repo, locator);
		assertThat(service.getCamerasRepository(), sameInstance(repo));
		assertThat(service.getCameraLocator(), sameInstance(locator));
	}
	
	@Test(expected=NullPointerException.class)
	public void testCameraServiceNullCamerasRepository() {
		CameraLocator locator = mock(CameraLocator.class);
		new CameraService(null, locator);
	}

	@Test(expected=NullPointerException.class)
	public void testCameraServiceNullCameraLocator() {
		CamerasRepository repo = mock(CamerasRepository.class);
		new CameraService(repo, null);
	}

	@Test
	public void testGetCameras() throws RepositoryException {
		Cameras cameras = mock(Cameras.class);
		CamerasRepository repo = when(mock(CamerasRepository.class).getCameras()).thenReturn(cameras).getMock();
		CameraLocator locator = mock(CameraLocator.class);
		
		CameraService service = new CameraService(repo, locator);
		assertThat(service.getCameras(), sameInstance(cameras));
	}

	@Test
	public void testGetCamerasForRoute() throws RepositoryException {
		Cameras cameras = mock(Cameras.class);
		CamerasRepository repo = when(mock(CamerasRepository.class).getCameras()).thenReturn(cameras).getMock();
		CameraLocator locator = mock(CameraLocator.class);

		CameraService service = new CameraService(repo, locator);
		
		Route route = mock(Route.class);
		Cameras result = service.getCameras(route);

		assertThat(result, not(nullValue()));
		verify(locator).locate(cameras, new Cameras(), route);
	}

	@Test(expected=NullPointerException.class)
	public void testGetCamerasForRouteNullRoute() throws RepositoryException {
		new CameraService(mock(CamerasRepository.class), mock(CameraLocator.class)).getCameras(null);
	}

}