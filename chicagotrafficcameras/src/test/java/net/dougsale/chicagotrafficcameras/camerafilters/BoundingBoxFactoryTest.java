package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.junit.Assert.*;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.Route.Step;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Map;

public class BoundingBoxFactoryTest {

	@Test
	public void testBoundingBoxFactory() {
		double padding = 0.0001;
		BoundingBoxFactory factory = new BoundingBoxFactory(padding);
		assertThat(factory.getPadding(), equalTo(padding));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testBoundingBoxFactoryIllegalPadddingParameter() {
		new BoundingBoxFactory(-0.0001);
	}

	@Test
	public void testGetCameraFilters() {
		Step step1 = mock(Step.class);
		when(step1.getStart()).thenReturn(new Location(0.0, 0.0));
		when(step1.getEnd()).thenReturn(new Location(1.0, 1.0));
		
		Step step2 = mock(Step.class);
		when(step2.getStart()).thenReturn(new Location(1.0, 1.0));
		when(step2.getEnd()).thenReturn(new Location(2.0, 1.2));
		
		Route route = when(mock(Route.class).getSteps()).thenReturn(Arrays.asList(step1, step2)).getMock();

		BoundingBoxFactory factory = new BoundingBoxFactory(0.1);
		Map<Step,CameraFilter> filters = factory.getCameraFilters(route);
		assertThat(filters.size(), equalTo(2));
		
		CameraFilter filter1 = filters.get(step1);
		assertThat(filter1, equalTo(new BoundingBox(new Location(-0.1, -0.1), new Location(1.1, 1.1))));

		CameraFilter filter2 = filters.get(step2);
		assertThat(filter2, equalTo(new BoundingBox(new Location(0.9, 0.9), new Location(2.1, 1.3))));
	}

	@Test(expected=NullPointerException.class)
	public void testGetCameraFiltersNullRoute() {
		new BoundingBoxFactory(0.0001).getCameraFilters(null);
	}

}
