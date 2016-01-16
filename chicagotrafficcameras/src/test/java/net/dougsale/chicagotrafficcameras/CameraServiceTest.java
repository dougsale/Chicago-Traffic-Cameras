package net.dougsale.chicagotrafficcameras;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.CamerasFactory;
import net.dougsale.chicagotrafficcameras.repository.RepositoryException;

public class CameraServiceTest {

	@Test
	public void testCameraService() {
		CamerasFactory factory = mock(CamerasFactory.class);
		CameraLocator locator = mock(CameraLocator.class);
		
		CameraService service = new CameraService(factory, locator);
		assertThat(service.getCamerasFactory(), sameInstance(factory));
		assertThat(service.getCameraLocator(), sameInstance(locator));
	}
	
	@Test(expected=NullPointerException.class)
	public void testCameraServiceNullCamerasFactory() {
		CameraLocator locator = mock(CameraLocator.class);
		new CameraService(null, locator);
	}

	@Test(expected=NullPointerException.class)
	public void testCameraServiceNullCameraLocator() {
		CamerasFactory factory = mock(CamerasFactory.class);
		new CameraService(factory, null);
	}

	@Test
	public void testGetCameras() throws RepositoryException {
		Cameras cameras = mock(Cameras.class);
		CamerasFactory factory = when(mock(CamerasFactory.class).getAllCameras()).thenReturn(cameras).getMock();
		CameraLocator locator = mock(CameraLocator.class);
		
		CameraService service = new CameraService(factory, locator);
		assertThat(service.getCameras(), sameInstance(cameras));
	}

	@Test
	public void testGetCamerasForRoute() throws RepositoryException {
		CamerasFactory factory = mock(CamerasFactory.class);
		Route route = mock(Route.class);
		Cameras cameras = mock(Cameras.class);
		CameraLocator locator = when(mock(CameraLocator.class).locate(route)).thenReturn(cameras).getMock();

		CameraService service = new CameraService(factory, locator);		
		service.getCameras(route);

		verify(factory, never()).getAllCameras();
		verify(locator, times(1)).locate(route);
	}

	@Test(expected=NullPointerException.class)
	public void testGetCamerasForRouteNullRoute() throws RepositoryException {
		new CameraService(mock(CamerasFactory.class), mock(CameraLocator.class)).getCameras(null);
	}

}