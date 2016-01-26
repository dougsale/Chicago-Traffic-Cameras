package net.dougsale.chicagotrafficcameras.webapp;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.repository.RepositoryException;
import net.dougsale.chicagotrafficcameras.services.CamerasService;
import net.dougsale.chicagotrafficcameras.webapp.mappers.CamerasToJsonMapper;
import net.dougsale.chicagotrafficcameras.webapp.mappers.MapperException;
import net.dougsale.chicagotrafficcameras.webapp.mappers.RouteFromJsonMapper;

public class CamerasControllerTest {

	@Test
	public void testCamerasController() {
		CamerasService service = mock(CamerasService.class);
		RouteFromJsonMapper routeMapper = mock(RouteFromJsonMapper.class);
		CamerasToJsonMapper camerasMapper = mock(CamerasToJsonMapper.class);
		
		CamerasController controller = new CamerasController(service, routeMapper, camerasMapper);
		assertThat(controller.getCamerasService(), sameInstance(service));
		assertThat(controller.getRouteFromJsonMapper(), sameInstance(routeMapper));
		assertThat(controller.getCamerasToJsonMapper(), sameInstance(camerasMapper));
	}
	
	@Test(expected=NullPointerException.class)
	public void testCamerasControllerNullCameraService() {
		new CamerasController(null, mock(RouteFromJsonMapper.class), mock(CamerasToJsonMapper.class));
	}

	@Test(expected=NullPointerException.class)
	public void testCamerasControllerNullRouteFromJsonMapper() {
		new CamerasController(mock(CamerasService.class), null, mock(CamerasToJsonMapper.class));
	}

	@Test(expected=NullPointerException.class)
	public void testCamerasControllerNullCamerasToJsonMapper() {
		new CamerasController(mock(CamerasService.class), mock(RouteFromJsonMapper.class), null);
	}

	@Test
	public void testGetAllCameras() throws RepositoryException, MapperException {
		RouteFromJsonMapper routeMapper = mock(RouteFromJsonMapper.class);
		
		CamerasService service = mock(CamerasService.class);
		Cameras cameras = mock(Cameras.class);
		when(service.getCameras()).thenReturn(cameras);
		
		CamerasToJsonMapper cameraMapper = mock(CamerasToJsonMapper.class);
		String json = "{}";
		when(cameraMapper.map(cameras)).thenReturn(json);
		
		CamerasController controller = new CamerasController(service, routeMapper, cameraMapper);
		assertThat(controller.getAllCameras(), equalTo(json));
		
		verify(service, times(1)).getCameras();
		verify(cameraMapper, times(1)).map(cameras);
	}

	@Test
	public void testGetCameras() throws RepositoryException, MapperException {
		String json = "{}";
		Route route = mock(Route.class);
		Cameras cameras = mock(Cameras.class);

		RouteFromJsonMapper routeMapper = mock(RouteFromJsonMapper.class);
		when(routeMapper.map(json)).thenReturn(route);

		CamerasService cameraService = mock(CamerasService.class);
		when(cameraService.getCameras(route)).thenReturn(cameras);
		
		CamerasToJsonMapper camerasMapper = mock(CamerasToJsonMapper.class);
		when(camerasMapper.map(cameras)).thenReturn(json);
		
		CamerasController controller = new CamerasController(cameraService, routeMapper, camerasMapper);
		assertThat(controller.getCameras(json), equalTo(json));
		
		verify(routeMapper, times(1)).map(json);
		verify(cameraService, times(1)).getCameras(route);
		verify(camerasMapper, times(1)).map(cameras);
	}

}
