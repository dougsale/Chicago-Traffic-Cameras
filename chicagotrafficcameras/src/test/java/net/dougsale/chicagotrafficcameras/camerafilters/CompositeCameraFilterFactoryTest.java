package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.Route.Step;

public class CompositeCameraFilterFactoryTest {

	private CameraFilterFactory[] factories = new CameraFilterFactory[] {
			mock(CameraFilterFactory.class),
			mock(CameraFilterFactory.class),
			mock(CameraFilterFactory.class)
		};
		
	@Test
	public void testCompositeCameraFilterFactory() {
		CompositeCameraFilterFactory factory = new CompositeCameraFilterFactory(factories);		
		assertThat(factory.getCameraFilterFactories(), equalTo(factories));
	}
	
	// test immutability
	
	@Test
	public void testImmutableFactoriesParameterCopied() {
		CameraFilterFactory[] factories = Arrays.copyOf(this.factories, this.factories.length);
		CompositeCameraFilterFactory factory = new CompositeCameraFilterFactory(factories);		
		factories[2] = null;
		assertThat(factory.getCameraFilterFactories(), not(equalTo(factories)));
		assertThat(factory.getCameraFilterFactories(), equalTo(this.factories));
	}

	@Test
	public void testImmutableFactoriesReturnValueCopied() {
		CompositeCameraFilterFactory factory = new CompositeCameraFilterFactory(factories);		
		CameraFilterFactory[] factories = factory.getCameraFilterFactories();
		factories[2] = factories[0];
		assertThat(factory.getCameraFilterFactories(), not(equalTo(factories)));
		assertThat(factory.getCameraFilterFactories(), equalTo(this.factories));
	}
	
	// test invalid constructor parameters
	
	@Test(expected=NullPointerException.class)
	public void testCompositeCameraFilterFactoryNullFactories() {
		new CompositeCameraFilterFactory((CameraFilterFactory[])null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCompositeCameraFilterFactoryEmptyFactories() {
		new CompositeCameraFilterFactory(new CameraFilterFactory[] {});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCompositeCameraFilterFactorySingleFactory() {
		new CompositeCameraFilterFactory(new CameraFilterFactory[] { mock(CameraFilterFactory.class) });
	}

	@Test(expected=NullPointerException.class)
	public void testCompositeCameraFilterFactoryNullFactoriesComponent() {
		new CompositeCameraFilterFactory(new CameraFilterFactory[] { mock(CameraFilterFactory.class), null });
	}

	// the heart of it
	
	@Test
	public void testGetCameraFilters() {
		
		// create a route with 3 steps and 2 filter factories that return
		// unique filters for each step
		
		CameraFilterFactory f1 = mock(CameraFilterFactory.class);
		CameraFilterFactory f2 = mock(CameraFilterFactory.class);

		CompositeCameraFilterFactory factory = new CompositeCameraFilterFactory(f1, f2);

		Step s1 = mock(Step.class);
		Step s2 = mock(Step.class);
		Step s3 = mock(Step.class);

		CameraFilter f1s1 = mock(CameraFilter.class);
		CameraFilter f1s2 = mock(CameraFilter.class);
		CameraFilter f1s3 = mock(CameraFilter.class);
		CameraFilter f2s1 = mock(CameraFilter.class);
		CameraFilter f2s2 = mock(CameraFilter.class);
		CameraFilter f2s3 = mock(CameraFilter.class);
		
		Route route = mock(Route.class);
		when(route.getSteps()).thenReturn(Arrays.asList(s1, s2, s3));

		Map<Step,CameraFilter> m1 = new HashMap<>();
		m1.put(s1, f1s1);
		m1.put(s2, f1s2);
		m1.put(s3, f1s3);
		when(f1.getCameraFilters(route)).thenReturn(m1);

		Map<Step,CameraFilter> m2 = new HashMap<>();
		m2.put(s1, f2s1);
		m2.put(s2, f2s2);
		m2.put(s3, f2s3);
		when(f2.getCameraFilters(route)).thenReturn(m2);
		
		// this is the method under test
		Map<Step,CameraFilter> filters = factory.getCameraFilters(route);
		
		verify(f1).getCameraFilters(route);
		verify(f2).getCameraFilters(route);
		assertThat(filters.size(), equalTo(3 /* num steps */));
		
		// check that each composite camera filter is comprised of the correct filters
		
		assertThat(filters.get(s1), instanceOf(CompositeCameraFilter.class));
		CompositeCameraFilter cf1 = (CompositeCameraFilter) filters.get(s1);
		assertThat(cf1.getCameraFilters(), equalTo(new CameraFilter[] { f1s1, f2s1 }));
		
		assertThat(filters.get(s2), instanceOf(CompositeCameraFilter.class));
		CompositeCameraFilter cf2 = (CompositeCameraFilter) filters.get(s2);
		assertThat(cf2.getCameraFilters(), equalTo(new CameraFilter[] { f1s2, f2s2 }));
		
		assertThat(filters.get(s3), instanceOf(CompositeCameraFilter.class));
		CompositeCameraFilter cf3 = (CompositeCameraFilter) filters.get(s3);
		assertThat(cf3.getCameraFilters(), equalTo(new CameraFilter[] { f1s3, f2s3 }));
	}

	// test invalid route parameter
	@Test(expected=NullPointerException.class)
	public void testGetCameraFiltersNullRoute() {
		new CompositeCameraFilterFactory(factories).getCameraFilters(null);
	}

}
