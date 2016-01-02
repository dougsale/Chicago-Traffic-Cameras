package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Camera;
import net.dougsale.chicagotrafficcameras.domain.Location;

public class CompositeCameraFilterTest {

	private CameraFilter[] filters = new CameraFilter[] {
			mock(CameraFilter.class),
			mock(CameraFilter.class),
			mock(CameraFilter.class)
		};
		
	@Test
	public void testCompositeCameraFilter() {
		CompositeCameraFilter filter = new CompositeCameraFilter(filters);		
		assertThat(filter.getCameraFilters(), equalTo(filters));
	}
	
	// test immutability
	
	@Test
	public void testImmutableFiltersParameterCopied() {
		CameraFilter[] filters = Arrays.copyOf(this.filters, this.filters.length);
		CompositeCameraFilter filter = new CompositeCameraFilter(filters);		
		filters[2] = null;
		assertThat(filter.getCameraFilters(), not(equalTo(filters)));
		assertThat(filter.getCameraFilters(), equalTo(this.filters));
	}

	@Test
	public void testImmutableFiltersReturnValueCopied() {
		CompositeCameraFilter filter = new CompositeCameraFilter(filters);		
		CameraFilter[] filters = filter.getCameraFilters();
		filters[2] = filters[0];
		assertThat(filter.getCameraFilters(), not(equalTo(filters)));
		assertThat(filter.getCameraFilters(), equalTo(this.filters));
	}
	
	// test invalid constructor parameters
	
	@Test(expected=NullPointerException.class)
	public void testCompositeCameraFilterNullFilters() {
		new CompositeCameraFilter((CameraFilter[])null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCompositeCameraFilterEmptyFilters() {
		new CompositeCameraFilter(new CameraFilter[] {});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCompositeCameraFilterSingleFilter() {
		new CompositeCameraFilter(new CameraFilter[] { mock(CameraFilter.class) });
	}

	@Test(expected=NullPointerException.class)
	public void testCompositeCameraFilterNullFiltersComponent() {
		new CompositeCameraFilter(new CameraFilter[] { mock(CameraFilter.class), null });
	}

	@Test
	public void testAcceptAllAccept() {
		CompositeCameraFilter filter = new CompositeCameraFilter(filters);
		Camera camera = mock(Camera.class);
		
		when(filters[0].accept(camera)).thenReturn(true);
		when(filters[1].accept(camera)).thenReturn(true);
		when(filters[2].accept(camera)).thenReturn(true);
		
		boolean result = filter.accept(camera);
		assertThat(result, equalTo(true));
		
		verify(filters[0]).accept(camera);
		verify(filters[1]).accept(camera);
		verify(filters[2]).accept(camera);
	}

	@Test
	public void testAcceptThirdRejects() {
		CompositeCameraFilter filter = new CompositeCameraFilter(filters);
		Camera camera = mock(Camera.class);
		
		when(filters[0].accept(camera)).thenReturn(true);
		when(filters[1].accept(camera)).thenReturn(true);
		when(filters[2].accept(camera)).thenReturn(false);
		
		boolean result = filter.accept(camera);
		assertThat(result, equalTo(false));
		
		verify(filters[0]).accept(camera);
		verify(filters[1]).accept(camera);
		verify(filters[2]).accept(camera);
	}

	@Test
	public void testAcceptSecondRejects() {
		CompositeCameraFilter filter = new CompositeCameraFilter(filters);
		Camera camera = mock(Camera.class);
		
		when(filters[0].accept(camera)).thenReturn(true);
		when(filters[1].accept(camera)).thenReturn(false);
		when(filters[2].accept(camera)).thenReturn(true);
		
		boolean result = filter.accept(camera);
		assertThat(result, equalTo(false));
		
		verify(filters[0]).accept(camera);
		verify(filters[1]).accept(camera);
		verify(filters[2], never()).accept(camera);
	}

	@Test
	public void testAcceptFirstRejects() {
		CompositeCameraFilter filter = new CompositeCameraFilter(filters);
		Camera camera = mock(Camera.class);
		
		when(filters[0].accept(camera)).thenReturn(false);
		when(filters[1].accept(camera)).thenReturn(true);
		when(filters[2].accept(camera)).thenReturn(false);
		
		boolean result = filter.accept(camera);
		assertThat(result, equalTo(false));
		
		verify(filters[0]).accept(camera);
		verify(filters[1], never()).accept(camera);
		verify(filters[2], never()).accept(camera);
	}

	@Test(expected=NullPointerException.class)
	public void testAcceptNullCamera() {
		new CompositeCameraFilter(filters).accept(null);
	}

	@Test
	public void testEqualsHashCode() {
		CameraFilter component1 = mock(CameraFilter.class);
		CameraFilter component2 = mock(CameraFilter.class);

		CompositeCameraFilter filter1 = new CompositeCameraFilter(component1, component2);
		CompositeCameraFilter filter2 = new CompositeCameraFilter(component1, component2);
		assertThat(filter1, equalTo(filter2));
		assertThat(filter2, equalTo(filter1));
		assertThat(filter1.hashCode(), equalTo(filter2.hashCode()));
		
		CompositeCameraFilter filter3 = new CompositeCameraFilter(component1, mock(CameraFilter.class));
		assertThat(filter1, not(equalTo(filter3)));
		assertThat(filter3, not(equalTo(filter1)));
		
		CompositeCameraFilter filter4 = new CompositeCameraFilter(component1, component2, mock(CameraFilter.class));
		assertThat(filter1, not(equalTo(filter4)));
		assertThat(filter4, not(equalTo(filter1)));
		
		// order does matter
		CompositeCameraFilter filter5 = new CompositeCameraFilter(component2, component1);
		assertThat(filter1, not(equalTo(filter5)));
		assertThat(filter5, not(equalTo(filter1)));
	}
		

}
