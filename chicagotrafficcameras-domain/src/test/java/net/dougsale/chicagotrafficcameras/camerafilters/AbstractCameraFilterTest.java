package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Camera;

public class AbstractCameraFilterTest {

	@Test
	public void testFilter() {
		Camera
			good = mock(Camera.class),
			bad = mock(Camera.class);
		
		Cameras cameras = when(mock(Cameras.class).get()).thenReturn(new HashSet<>(Arrays.asList(good, bad))).getMock();
		
		AbstractCameraFilter filter = mock(AbstractCameraFilter.class);
		when(filter.filter(cameras)).thenCallRealMethod();
		when(filter.accept(good)).thenReturn(true);
		when(filter.accept(bad)).thenReturn(false);
		
		Cameras result = filter.filter(cameras);
		assertThat(result, sameInstance(cameras));
		
		verify(result).remove(bad);
		verify(result, never()).remove(good);
	}

}
