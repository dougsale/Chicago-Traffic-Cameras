package net.dougsale.chicagotrafficcameras.camerafilters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.Directions;
import net.dougsale.chicagotrafficcameras.Directions.Step;
import net.dougsale.chicagotrafficcameras.DirectionsTestUtility;
import net.dougsale.chicagotrafficcameras.domain.Approach;
import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

public class StreetMatcherFactoryTest {

	String _201NColumbusDr_4800WGrandAve =
			"{\"startAddress\":\"201 N Columbus Dr, Chicago, IL 60611, USA\",\"endAddress\":\"4800 W Grand Ave, Chicago, IL 60639, USA\",\"steps\":[{\"instructions\":\"Head <b>south</b> on <b>N Columbus Dr</b> toward <b>E Lower Wacker Dr</b>\",\"start\":{\"latitude\":41.8880992,\"longitude\":-87.6207005},\"end\":{\"latitude\":41.8858565,\"longitude\":-87.6207243}},{\"instructions\":\"Continue straight to stay on <b>N Columbus Dr</b>\",\"start\":{\"latitude\":41.8858565,\"longitude\":-87.6207243},\"end\":{\"latitude\":41.8761999,\"longitude\":-87.62062029999998}},{\"instructions\":\"Turn <b>right</b> onto <b>E Congress Pkwy</b>\",\"start\":{\"latitude\":41.8761999,\"longitude\":-87.62062029999998},\"end\":{\"latitude\":41.8756942,\"longitude\":-87.63461159999997}},{\"instructions\":\"Continue onto <b>I-290 W</b> (signs for <b>I-90</b>)\",\"start\":{\"latitude\":41.8756942,\"longitude\":-87.63461159999997},\"end\":{\"latitude\":41.8740036,\"longitude\":-87.73096279999999}},{\"instructions\":\"Take exit <b>25</b> toward <b>Kostner Ave</b>\",\"start\":{\"latitude\":41.8740036,\"longitude\":-87.73096279999999},\"end\":{\"latitude\":41.8742439,\"longitude\":-87.73473709999996}},{\"instructions\":\"Merge onto <b>W Congress Pkwy</b>\",\"start\":{\"latitude\":41.8742439,\"longitude\":-87.73473709999996},\"end\":{\"latitude\":41.8740673,\"longitude\":-87.745022}},{\"instructions\":\"Turn <b>right</b> onto <b>S Cicero Ave</b>/<b>Mandela Rd</b>\",\"start\":{\"latitude\":41.8740673,\"longitude\":-87.745022},\"end\":{\"latitude\":41.9138915,\"longitude\":-87.74589079999998}},{\"instructions\":\"Turn <b>left</b> onto <b>W Grand Ave</b><div style=\\\"font-size:0.9em\\\">Destination will be on the right</div>\",\"start\":{\"latitude\":41.9138915,\"longitude\":-87.74589079999998},\"end\":{\"latitude\":41.9141465,\"longitude\":-87.7465512}}]}";
	
	@Test(expected=NullPointerException.class)
	public void testStreetMatcherFactoryNullArg() {
		new StreetMatcherFactory(null);
	}

	@Test(expected=NullPointerException.class)
	public void testGetNullArg() {
		StreetMatcherFactory factory = new StreetMatcherFactory(mock(Directions.class));
		factory.get(null);
	}

	@Test
	public void testGet() {
		Location mockLocation = mock(Location.class);		
		List<Step> steps = Arrays.asList(
			new Step("Make a <b>U-turn</b> at <b>W Superior St</b>", mockLocation, mockLocation),
			new Step("Lather. Rinse. Repeat.", mockLocation, mockLocation)
		);
		Directions directions = new Directions("123 Foo Way", "789 Bar Dr", steps);
		StreetMatcherFactory factory = new StreetMatcherFactory(directions);

		// a step with a street should return a matcher
		SpeedCamera camera = new SpeedCamera("134 Foo Way", mockLocation, EnumSet.of(Approach.EASTBOUND));
		StreetMatcher matcher = factory.get(directions.steps.get(0));
		assertThat(matcher.match(camera), is(true));

		// a step with an indeterminate street should always match (false positive better than false negative)
		assertThat(factory.get(directions.steps.get(1)), sameInstance(StreetMatcherFactory.streetMatcherAlways));

		// a step not in the directions should never match
		assertThat(factory.get(mock(Step.class)), sameInstance(StreetMatcherFactory.streetMatcherNever));
	}

	@Test
	public void testGetStreetForStep() {
		Directions directions = DirectionsTestUtility.getDirections(_201NColumbusDr_4800WGrandAve);
		StreetMatcherFactory factory = new StreetMatcherFactory(directions);
		Iterator<Step> iter = directions.steps.iterator();
		
		assertThat(factory.getStreetForStep(iter.next()), equalTo("N Columbus Dr"));
		assertThat(factory.getStreetForStep(iter.next()), equalTo("N Columbus Dr"));
		assertThat(factory.getStreetForStep(iter.next()), equalTo("E Congress Pkwy"));
		assertThat(factory.getStreetForStep(iter.next()), equalTo("I-290 W"));
		assertThat(factory.getStreetForStep(iter.next()), equalTo("I-290 W (exit 25)"));
		assertThat(factory.getStreetForStep(iter.next()), equalTo("W Congress Pkwy"));
		assertThat(factory.getStreetForStep(iter.next()), equalTo("S Cicero Ave/Mandela Rd"));
		assertThat(factory.getStreetForStep(iter.next()), equalTo("W Grand Ave"));
	}

	@Test
	public void testCreateStreetForStepMapping() {
		Directions directions = DirectionsTestUtility.getDirections(_201NColumbusDr_4800WGrandAve);
		StreetMatcherFactory factory = new StreetMatcherFactory(directions);
		Iterator<Step> iter = directions.steps.iterator();

		Map<Step, String> map = factory.createStreetForStepMapping(directions);

		assertThat(map.size(), equalTo(8));
		assertThat(map.get(iter.next()), equalTo("N Columbus Dr"));
		assertThat(map.get(iter.next()), equalTo("N Columbus Dr"));
		assertThat(map.get(iter.next()), equalTo("E Congress Pkwy"));
		assertThat(map.get(iter.next()), equalTo("I-290 W"));
		assertThat(map.get(iter.next()), equalTo("I-290 W (exit 25)"));
		assertThat(map.get(iter.next()), equalTo("W Congress Pkwy"));
		assertThat(map.get(iter.next()), equalTo("S Cicero Ave/Mandela Rd"));
		assertThat(map.get(iter.next()), equalTo("W Grand Ave"));
	}

	@Test
	public void testExtractStreetFromAddress() {
		StreetMatcherFactory factory = new StreetMatcherFactory(mock(Directions.class));
		assertThat(factory.extractStreetFromAddress("615 N Ogden Ave, Chicago, IL 60642, USA"), equalTo("N Ogden Ave"));
		assertThat(factory.extractStreetFromAddress("201 N Columbus Dr, Chicago, IL 60611, USA"), equalTo("N Columbus Dr"));
	}

	@Test
	public void testExtractStreetFromInstructions() {
		StreetMatcherFactory factory = new StreetMatcherFactory(mock(Directions.class));
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
