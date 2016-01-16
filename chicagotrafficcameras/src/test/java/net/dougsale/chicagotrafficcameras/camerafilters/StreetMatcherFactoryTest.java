/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.Route.Step;

public class StreetMatcherFactoryTest {

	@Test
	public void testGetCameraFilters() {
		Step step1 = when(mock(Step.class).getInstructions()).thenReturn("Make a <b>U-turn</b> at <b>W Superior St</b>").getMock(); 
		Step step2 = when(mock(Step.class).getInstructions()).thenReturn("Lather. Rinse. Repeat.").getMock();
		List<Step> steps = Arrays.asList(step1, step2);

		Route route = mock(Route.class);
		when(route.getStartAddress()).thenReturn("201 N Columbus Dr, Chicago, IL 60611, USA");
		when(route.getSteps()).thenReturn(steps);

		StreetMatcherFactory factory = new StreetMatcherFactory();
		Map<Step,CameraFilter> filters = factory.getCameraFilters(route);
		assertThat(filters.size(), equalTo(2));
		
		CameraFilter filter1 = filters.get(step1);
		assertThat(filter1, equalTo(new StreetMatcher("N Columbus Dr")));

		CameraFilter filter2 = filters.get(step2);
		assertThat(filter2, sameInstance(StreetMatcherFactory.streetMatcherAlways));
	}

	@Test(expected=NullPointerException.class)
	public void testGetCameraFiltersNullRoute() {
		new StreetMatcherFactory().getCameraFilters(null);
	}

	@Test
	public void testExtractStreetFromAddress() {
		StreetMatcherFactory factory = new StreetMatcherFactory();
		assertThat(factory.extractStreetFromAddress("615 N Ogden Ave, Chicago, IL 60642, USA"), equalTo("N Ogden Ave"));
		assertThat(factory.extractStreetFromAddress("201 N Columbus Dr, Chicago, IL 60611, USA"), equalTo("N Columbus Dr"));
	}

	@Test
	public void testExtractStreetFromInstructions() {
		StreetMatcherFactory factory = new StreetMatcherFactory();
		assertThat(factory.extractStreetFromInstructions("Make a <b>U-turn</b> at <b>W Superior St</b>", "Prior St"), equalTo("Prior St"));
		assertThat(factory.extractStreetFromInstructions("Keep <b>left</b> to continue on <b>Historic U.S. 66 W</b>/<b>W Ogden Ave</b>", null), equalTo("Historic U.S. 66 W/W Ogden Ave"));
		assertThat(factory.extractStreetFromInstructions("Continue onto <b>I-290 W</b> (signs for <b>I-90</b>)", "Prior St"), equalTo("I-290 W"));
		assertThat(factory.extractStreetFromInstructions("Take exit <b>25</b> toward <b>Kostner Ave</b>", "I-290 W"), equalTo("I-290 W (exit 25)"));
		assertThat(factory.extractStreetFromInstructions("Turn <b>left</b> onto <b>W Ogden Ave</b><div style=\"font-size:0.9em\">Destination will be on the right</div>", "Prior St"), equalTo("W Ogden Ave"));
		assertThat(factory.extractStreetFromInstructions("Continue onto <b>N Central Park Ave</b>", "Prior St"), equalTo("N Central Park Ave"));
		// note that this method returns null if it can't determine the street from the instructions
		assertThat(factory.extractStreetFromInstructions("Lather. Rinse. Repeat.", "Prior St"), is(nullValue()));
	}

}