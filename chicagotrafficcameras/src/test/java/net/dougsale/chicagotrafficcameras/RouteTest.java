package net.dougsale.chicagotrafficcameras;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.Route.Step;
import net.dougsale.chicagotrafficcameras.domain.Location;

public class RouteTest {

	String startAddress = "123 Start";
	String endAddress = "789 End";
	List<Step> steps = Arrays.asList(
		new Step("step 1 instructions", new Location(1.0, 1.0), new Location(1.2, 1.2)),
		new Step("step 2 instructions", new Location(1.2, 1.2), new Location(2.0, 2.0))
	);

	@Test
	public void testRoute() {
		Route route = new Route(startAddress, endAddress, steps);
		assertThat(route.getStartAddress(), equalTo(startAddress));
		assertThat(route.getEndAddress(), equalTo(endAddress));
		assertThat(route.getSteps(), equalTo(steps));
	}
	
	// Immutable tests
	
	@Test
	public void testRouteImmutableStepsParameterCopied() {
		List<Step> steps = new ArrayList<>(this.steps);
		Route route = new Route(startAddress, endAddress, steps);
		steps.clear();
		assertThat(route.getSteps(), not(equalTo(steps)));		
		assertThat(route.getSteps(), equalTo(this.steps));		
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRouteImmutableStepsUnmodifiable() {
		Route route = new Route(startAddress, endAddress, steps);
		route.getSteps().clear();
	}

	// Invalid parameters tests
	
	@Test(expected=NullPointerException.class)
	public void testRouteNullStartAddress() {
		new Route(null, endAddress, steps);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRouteEmptyStartAddress() {
		new Route("  ", endAddress, steps);
	}

	@Test(expected=NullPointerException.class)
	public void testRouteNullEndAddress() {
		new Route(startAddress, null, steps);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRouteEmptyEndAddress() {
		new Route(startAddress, "  ", steps);
	}

	@Test(expected=NullPointerException.class)
	public void testRouteNullSteps() {
		new Route(startAddress, endAddress, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRouteEmptySteps() {
		new Route(startAddress, endAddress, Collections.emptyList());
	}

	@Test(expected=NullPointerException.class)
	public void testRouteStepsNullElement() {
		new Route(startAddress, endAddress, Arrays.asList((Route.Step) null));
	}

	// test equals and hashcode
	
	@Test
	public void testRouteEqualsHashCode() {
		Route d1 = new Route(startAddress, endAddress, steps);
		Route d2 = new Route(startAddress, endAddress, steps);
		
		assertThat(d1, equalTo(d2));
		assertThat(d2, equalTo(d1));
		assertThat(d1.hashCode(), equalTo(d2.hashCode()));
		
		Route d3 = new Route("0 Foo Way", endAddress, steps);
		assertThat(d3, not(equalTo(d1)));
		
		Route d4 = new Route(startAddress, "0 Foo Way", steps);
		assertThat(d4, not(equalTo(d1)));
		
		List<Step> steps = new ArrayList<>(this.steps);
		steps.remove(1);
		
		Route d5 = new Route(startAddress, endAddress, steps);
		assertThat(d5, not(equalTo(d1)));
	}

	//-- Step -- Tests --
	
	String instructions = "instructions";
	Location start = new Location(1.0, 1.0);
	Location end = new Location(1.2, 1.2);
		
	@Test
	public void testStep() {
		Step step = new Step(instructions, start, end);
		assertThat(step.getInstructions(), equalTo(instructions));
		assertThat(step.getStart(), equalTo(start));
		assertThat(step.getEnd(), equalTo(end));
	}
	
	// invalid parameters tests
	
	@Test(expected=NullPointerException.class)
	public void testStepInstructionsNull() {
		new Step(null, start, end);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testStepInstructionsEmpty() {
		new Step(" ", start, end);
	}
	
	@Test(expected=NullPointerException.class)
	public void testStepStartNull() {
		new Step(instructions, null, end);
	}
	
	@Test(expected=NullPointerException.class)
	public void testStepEndNull() {
		new Step(instructions, start, null);
	}
	
	// test equals and hashcode
	
	@Test
	public void testStepEqualsHashCode() {
		Step s1 = new Step(instructions, start, end);
		Step s2 = new Step(instructions, start, end);
		
		assertThat(s1, equalTo(s2));
		assertThat(s2, equalTo(s1));
		assertThat(s1.hashCode(), equalTo(s2.hashCode()));
		
		Step s3 = new Step("s3 instructions", start, end);
		assertThat(s3, not(equalTo(s1)));
		
		Step s4 = new Step(instructions, new Location(0.0, 0.0), end);
		assertThat(s4, not(equalTo(s1)));
		
		Step s5 = new Step(instructions, start, new Location(0.0, 0.0));
		assertThat(s5, not(equalTo(s1)));
	}
}
