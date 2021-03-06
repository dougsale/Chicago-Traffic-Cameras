/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.CameraFilter;
import net.dougsale.chicagotrafficcameras.domain.CameraFilterFactory;
import net.dougsale.chicagotrafficcameras.domain.CameraLocator;
import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.CamerasFactory;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.Route.Step;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;
import net.dougsale.chicagotrafficcameras.repository.RepositoryException;

public class CameraLocatorTest {

	@Test
	public void testCameraLocator() {
		CamerasFactory camerasFactory = mock(CamerasFactory.class);
		CameraFilterFactory filterFactory = mock(CameraFilterFactory.class);
		CameraLocator locator = new CameraLocator(camerasFactory, filterFactory);
		assertThat(locator.getCamerasFactory(), sameInstance(camerasFactory));
		assertThat(locator.getCameraFilterFactory(), sameInstance(filterFactory));
	}
	
	@Test(expected=NullPointerException.class)
	public void testCameraLocatorNullCamerasFactory() {
		new CameraLocator(null, mock(CameraFilterFactory.class));
	}
	
	@Test(expected=NullPointerException.class)
	public void testCameraLocatorNullCameraFilterFactory() {
		new CameraLocator(mock(CamerasFactory.class), null);
	}
	
	@Test
	public void testLocate() throws RepositoryException {
		// the route taken
		Route route = mock(Route.class);
		Step step1 = mock(Step.class);
		Step step2 = mock(Step.class);
		Step step3 = mock(Step.class);
		List<Step> steps = Arrays.asList(step1, step2, step3);
		when(route.getSteps()).thenReturn(steps);
		
		// the deployed traffic cameras 
		Cameras allCameras = mock(Cameras.class);
		Camera camera = mock(Camera.class);
		RedLightCamera redLightCamera = mock(RedLightCamera.class);
		SpeedCamera speedCamera = mock(SpeedCamera.class);
		Set<Camera> cameraSet = new HashSet<>(Arrays.asList(camera, redLightCamera, speedCamera));
		when(allCameras.get()).thenReturn(cameraSet);
		
		// the cameras relevant to the route
		Cameras cameras = mock(Cameras.class);
		
		// the cameras factory
		CamerasFactory camerasFactory = mock(CamerasFactory.class);
		when(camerasFactory.getEmptyCameras()).thenReturn(cameras);
		when(camerasFactory.getAllCameras()).thenReturn(allCameras);
		
		// the filters that determine if a camera is germane to a particular step in the route
		CameraFilter filter1 = mock(CameraFilter.class);
		when(filter1.accept(camera)).thenReturn(false);
		when(filter1.accept(redLightCamera)).thenReturn(false);
		when(filter1.accept(speedCamera)).thenReturn(true);
		CameraFilter filter2 = mock(CameraFilter.class);
		when(filter2.accept(camera)).thenReturn(false);
		when(filter2.accept(redLightCamera)).thenReturn(false);
		when(filter2.accept(speedCamera)).thenReturn(true);
		CameraFilter filter3 = mock(CameraFilter.class);
		when(filter3.accept(camera)).thenReturn(false);
		when(filter3.accept(redLightCamera)).thenReturn(true);
		when(filter3.accept(speedCamera)).thenReturn(false);
		
		// the filter factory that creates the filters based on the route
		CameraFilterFactory factory = mock(CameraFilterFactory.class);
		Map<Step,CameraFilter> stepFilterMap = new HashMap<>();
		stepFilterMap.put(step1,  filter1);
		stepFilterMap.put(step2,  filter2);
		stepFilterMap.put(step3,  filter3);
		when(factory.getCameraFilters(route)).thenReturn(stepFilterMap);
		
		CameraLocator locator = new CameraLocator(camerasFactory, factory);
		Cameras result = locator.locate(route);
		
		// stepFilterMap was generated
		verify(factory, times(1)).getCameraFilters(route);
		// for each step
		verify(route, times(1)).getSteps();
		// all cameras were retrieved
		verify(allCameras, times(steps.size())).get();
		// and tested on each filter
		verify(filter1, times(cameraSet.size())).accept(any(Camera.class));
		verify(filter1, times(1)).accept(camera);
		verify(filter1, times(1)).accept(redLightCamera);
		verify(filter1, times(1)).accept(speedCamera);
		verify(filter2, times(cameraSet.size())).accept(any(Camera.class));
		verify(filter2, times(1)).accept(camera);
		verify(filter2, times(1)).accept(redLightCamera);
		verify(filter2, times(1)).accept(speedCamera);
		verify(filter3, times(cameraSet.size())).accept(any(Camera.class));
		verify(filter3, times(1)).accept(camera);
		verify(filter3, times(1)).accept(redLightCamera);
		verify(filter3, times(1)).accept(speedCamera);
		// verify that accepted cameras were added to the result
		assertThat(cameras, sameInstance(result));
		verify(result, never()).add(camera);
		verify(result, times(1)).add(redLightCamera);
		verify(result, times(2)).add(speedCamera);
		verify(result, times(3)).add(any(Camera.class));
	}	

	@Test(expected=NullPointerException.class)
	public void testLocateNullRoute() throws RepositoryException {
		new CameraLocator(mock(CamerasFactory.class), mock(CameraFilterFactory.class)).locate(null);
	}
	
//	@Test
//	public void testLocateCameras_615_N_Ogden_Ave_3315_W_Ogden_Ave() {
//		// route with large diagonal steps, i.e., large bounding boxes; so taxes street matcher filtering
//		
//		String json = "{\"startAddress\":\"615 N Ogden Ave, Chicago, IL 60642, USA\",\"endAddress\":\"3315 W Ogden Ave, Chicago, IL 60623, USA\",\"steps\":[{\"instructions\":\"Head <b>northeast</b> on <b>N Ogden Ave</b> toward <b>W Erie St</b>\",\"start\":{\"latitude\":41.8929664,\"longitude\":-87.65731360000001},\"end\":{\"latitude\":41.8949373,\"longitude\":-87.65565119999997}},{\"instructions\":\"Make a <b>U-turn</b> at <b>W Superior St</b>\",\"start\":{\"latitude\":41.8949373,\"longitude\":-87.65565119999997},\"end\":{\"latitude\":41.8666981,\"longitude\":-87.6836672}},{\"instructions\":\"Keep <b>left</b> to continue on <b>Historic U.S. 66 W</b>/<b>W Ogden Ave</b>\",\"start\":{\"latitude\":41.8666981,\"longitude\":-87.6836672},\"end\":{\"latitude\":41.8571103,\"longitude\":-87.7078085}},{\"instructions\":\"Keep <b>right</b> to continue on <b>W Ogden Ave</b>\",\"start\":{\"latitude\":41.8571103,\"longitude\":-87.7078085},\"end\":{\"latitude\":41.8567775,\"longitude\":-87.7090442}},{\"instructions\":\"Make a <b>U-turn</b> at <b>S Christiana Ave</b><div style=\\\"font-size:0.9em\\\">Destination will be on the right</div>\",\"start\":{\"latitude\":41.8567775,\"longitude\":-87.7090442},\"end\":{\"latitude\":41.8566225,\"longitude\":-87.70858179999999}}]}";
//		Route route = TestUtility.getRoute(json);
//
//		CameraLocator locator = new CameraLocator(cameras);
//		Cameras located = locator.locate(route);
//		
//		assertThat(located.get(RedLightCamera.class).size(), equalTo(0));
//		assertThat(located.get(SpeedCamera.class).size(), equalTo(2));
//		
//		NavigableSet<SpeedCamera> cameras = located.get(SpeedCamera.class, Cameras.BY_LATITUDE);
//		Iterator<SpeedCamera> iter = cameras.iterator();
//		assertThat(iter.next(), equalTo(
//				new SpeedCamera("2900 W Ogden Ave", new Location(41.86040786445197, -87.69867209877323),
//						EnumSet.of(Approach.EASTBOUND, Approach.WESTBOUND))));
//		assertThat(iter.next(), equalTo(
//				new SpeedCamera("115 N Ogden", new Location(41.883191521391076, -87.66411469955516),
//						EnumSet.of(Approach.NORTHBOUND, Approach.SOUTHBOUND))));
//	}
//	
//	@Test
//	public void testLocateCameras_615_N_Ogden_Ave_3315_W_Ogden_Ave_newLocate() {
//		// route with large diagonal steps, i.e., large bounding boxes; so taxes street matcher filtering
//		
//		String json = "{\"startAddress\":\"615 N Ogden Ave, Chicago, IL 60642, USA\",\"endAddress\":\"3315 W Ogden Ave, Chicago, IL 60623, USA\",\"steps\":[{\"instructions\":\"Head <b>northeast</b> on <b>N Ogden Ave</b> toward <b>W Erie St</b>\",\"start\":{\"latitude\":41.8929664,\"longitude\":-87.65731360000001},\"end\":{\"latitude\":41.8949373,\"longitude\":-87.65565119999997}},{\"instructions\":\"Make a <b>U-turn</b> at <b>W Superior St</b>\",\"start\":{\"latitude\":41.8949373,\"longitude\":-87.65565119999997},\"end\":{\"latitude\":41.8666981,\"longitude\":-87.6836672}},{\"instructions\":\"Keep <b>left</b> to continue on <b>Historic U.S. 66 W</b>/<b>W Ogden Ave</b>\",\"start\":{\"latitude\":41.8666981,\"longitude\":-87.6836672},\"end\":{\"latitude\":41.8571103,\"longitude\":-87.7078085}},{\"instructions\":\"Keep <b>right</b> to continue on <b>W Ogden Ave</b>\",\"start\":{\"latitude\":41.8571103,\"longitude\":-87.7078085},\"end\":{\"latitude\":41.8567775,\"longitude\":-87.7090442}},{\"instructions\":\"Make a <b>U-turn</b> at <b>S Christiana Ave</b><div style=\\\"font-size:0.9em\\\">Destination will be on the right</div>\",\"start\":{\"latitude\":41.8567775,\"longitude\":-87.7090442},\"end\":{\"latitude\":41.8566225,\"longitude\":-87.70858179999999}}]}";
//		Route route = TestUtility.getRoute(json);
//
//		CameraLocator locator = new CameraLocator(cameras, new BoundingBoxFactory(0.0003), new StreetMatcherFactory());
//		Cameras located = locator.newLocate(route);
//		
//		assertThat(located.get(RedLightCamera.class).size(), equalTo(0));
//		assertThat(located.get(SpeedCamera.class).size(), equalTo(2));
//		
//		NavigableSet<SpeedCamera> cameras = located.get(SpeedCamera.class, Cameras.BY_LATITUDE);
//		Iterator<SpeedCamera> iter = cameras.iterator();
//		assertThat(iter.next(), equalTo(
//				new SpeedCamera("2900 W Ogden Ave", new Location(41.86040786445197, -87.69867209877323),
//						EnumSet.of(Approach.EASTBOUND, Approach.WESTBOUND))));
//		assertThat(iter.next(), equalTo(
//				new SpeedCamera("115 N Ogden", new Location(41.883191521391076, -87.66411469955516),
//						EnumSet.of(Approach.NORTHBOUND, Approach.SOUTHBOUND))));
//	}
//	
//	@Test
//	public void testLocateCameras_201_N_Columbus_Dr_400_E_Illinois_St() {
//		// route with large diagonal steps, i.e., large bounding boxes; so taxes street matcher filtering
//		
//		String json = "{\"startAddress\":\"201 N Columbus Dr, Chicago, IL 60611, USA\",\"endAddress\":\"400 E Illinois St, Chicago, IL 60611, USA\",\"steps\":[{\"instructions\":\"Head <b>south</b> on <b>N Columbus Dr</b> toward <b>E Lower Wacker Dr</b>\",\"start\":{\"latitude\":41.8880992,\"longitude\":-87.6207005},\"end\":{\"latitude\":41.88776319999999,\"longitude\":-87.62070829999999}},{\"instructions\":\"Make a <b>U-turn</b> at <b>E Lower Wacker Dr</b>\",\"start\":{\"latitude\":41.88776319999999,\"longitude\":-87.62070829999999},\"end\":{\"latitude\":41.8910117,\"longitude\":-87.62012340000001}},{\"instructions\":\"Turn <b>right</b> onto <b>E Illinois St</b><div style=\\\"font-size:0.9em\\\">Destination will be on the left</div>\",\"start\":{\"latitude\":41.8910117,\"longitude\":-87.62012340000001},\"end\":{\"latitude\":41.8910524,\"longitude\":-87.6178458}}]}";
//		Route route = TestUtility.getRoute(json);
//
//		CameraLocator locator = new CameraLocator(cameras);
//		Cameras located = locator.locate(route);
//		
//		assertThat(located.get(RedLightCamera.class).size(), equalTo(1));
//		assertThat(located.get(SpeedCamera.class).size(), equalTo(3));
//		
//		Set<RedLightCamera> rlCameras = located.get(RedLightCamera.class);
//		Iterator<RedLightCamera> rlIter = rlCameras.iterator();
//		assertThat(rlIter.next(), equalTo(
//				new RedLightCamera(new HashSet<>(Arrays.asList("Columbus", "Illinois")),
//						new Location(41.891002, -87.620224),
//						EnumSet.of(Approach.NORTHBOUND, Approach.SOUTHBOUND))));
//
//		NavigableSet<SpeedCamera> spCameras = located.get(SpeedCamera.class, Cameras.BY_LONGITUDE);
//		Iterator<SpeedCamera> spIter = spCameras.iterator();
//		assertThat(spIter.next(), equalTo(
//				new SpeedCamera("450 N Columbus Dr", new Location(41.890122352505166, -87.62041639513696),
//						EnumSet.of(Approach.SOUTHBOUND))));
//		assertThat(spIter.next(), equalTo(
//				new SpeedCamera("449 N Columbus Dr", new Location(41.89003775504898, -87.62017903427099),
//						EnumSet.of(Approach.NORTHBOUND))));
//		assertThat(spIter.next(), equalTo(
//				new SpeedCamera("319 E Illinois St", new Location(41.89091009572781, -87.61934752145963),
//						EnumSet.of(Approach.EASTBOUND))));
//	}
}
