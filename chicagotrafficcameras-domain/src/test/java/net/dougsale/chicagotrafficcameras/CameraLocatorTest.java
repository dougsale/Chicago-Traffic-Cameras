/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.InOrder;

import net.dougsale.chicagotrafficcameras.Route.Step;
import net.dougsale.chicagotrafficcameras.camerafilters.CameraFilter;
import net.dougsale.chicagotrafficcameras.camerafilters.CameraFilterFactory;

public class CameraLocatorTest {

	@Test
	public void testCameraLocator() {
		CameraFilterFactory factory = mock(CameraFilterFactory.class);
		CameraLocator locator = new CameraLocator(factory);
		assertThat(locator.getCameraFilterFactory(), sameInstance(factory));
	}
	
	@Test(expected=NullPointerException.class)
	public void testCameraLocatorNullCameraFilterFactory() {
		new CameraLocator(null);
	}
	
	@Test
	public void testLocate() {
		// steps in route
		Step step1 = mock(Step.class);
		Step step2 = mock(Step.class);
		List<Step> steps = Arrays.asList(step1, step2);

		// args to locate, route returns steps
		Cameras candidates = mock(Cameras.class);
		Cameras matches = mock(Cameras.class);
		Route route = when(mock(Route.class).getSteps()).thenReturn(steps).getMock();
		
		// camera filters and their filtered results for each step
		Cameras filtered1 = mock(Cameras.class);
		CameraFilter filter1 = when(mock(CameraFilter.class).filter(candidates)).thenReturn(filtered1).getMock();
		Cameras filtered2 = mock(Cameras.class);
		CameraFilter filter2 = when(mock(CameraFilter.class).filter(candidates)).thenReturn(filtered2).getMock();
		
		Map<Step,CameraFilter> filters = new HashMap<>();
		filters.put(step1, filter1);
		filters.put(step2, filter2);
		
		// filter factory with assembled step->filter data structure
		CameraFilterFactory factory = when(mock(CameraFilterFactory.class).getCameraFilters(route)).thenReturn(filters).getMock();
		
		CameraLocator locator = new CameraLocator(factory);		
		locator.locate(candidates, matches, route);
		
		// verify that locator processed filters correctly
		InOrder inOrder = inOrder(matches);
		inOrder.verify(matches).addAll(filtered1);
		inOrder.verify(matches).addAll(filtered2);
	}
	
	@Test(expected=NullPointerException.class)
	public void testLocateNullCandidateCameras() {
		new CameraLocator(mock(CameraFilterFactory.class)).locate(null, mock(Cameras.class), mock(Route.class));
	}
	
	@Test(expected=NullPointerException.class)
	public void testLocateNullMatchesCameras() {
		new CameraLocator(mock(CameraFilterFactory.class)).locate(mock(Cameras.class), null, mock(Route.class));
	}
	
	@Test(expected=NullPointerException.class)
	public void testLocateNullRoute() {
		new CameraLocator(mock(CameraFilterFactory.class)).locate(mock(Cameras.class), mock(Cameras.class), null);
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
