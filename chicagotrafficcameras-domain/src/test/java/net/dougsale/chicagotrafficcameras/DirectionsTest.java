package net.dougsale.chicagotrafficcameras;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.Directions.Step;
import net.dougsale.chicagotrafficcameras.domain.Location;

public class DirectionsTest {

	String startAddress = "123 Start";
	String endAddress = "789 End";
	List<Step> steps = Arrays.asList(
		new Step("step 1 instructions", new Location(1.0, 1.0), new Location(1.2, 1.2)),
		new Step("step 2 instructions", new Location(1.2, 1.2), new Location(2.0, 2.0))
	);

	@Test
	public void testDirections() {
		Directions directions = new Directions(startAddress, endAddress, steps);
		assertThat(directions.startAddress, equalTo(startAddress));
		assertThat(directions.endAddress, equalTo(endAddress));
		assertThat(directions.steps, equalTo(steps));
	}
	
	// Immutable tests
	
	@Test
	public void testDirectionsImmutableStepsListParameterCopied() {
		List<Step> steps = new ArrayList<>(this.steps);
		Directions directions = new Directions(startAddress, endAddress, steps);
		steps.clear();
		assertThat(directions.steps, equalTo(this.steps));		
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testDirectionsImmutableStepsUnmodifiable() {
		Directions directions = new Directions(startAddress, endAddress, steps);
		directions.steps.clear();
	}

	// Invalid parameters tests
	
	@Test(expected=NullPointerException.class)
	public void testDirectionsNullStartAddress() {
		new Directions(null, endAddress, steps);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testDirectionsEmptyStartAddress() {
		new Directions("  ", endAddress, steps);
	}

	@Test(expected=NullPointerException.class)
	public void testDirectionsNullEndAddress() {
		new Directions(startAddress, null, steps);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testDirectionsEmptyEndAddress() {
		new Directions(startAddress, "  ", steps);
	}

	@Test(expected=NullPointerException.class)
	public void testDirectionsNullSteps() {
		new Directions(startAddress, endAddress, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testDirectionsEmptySteps() {
		new Directions(startAddress, endAddress, Collections.emptyList());
	}

	@Test(expected=NullPointerException.class)
	public void testDirectionsStepsNullElement() {
		new Directions(startAddress, endAddress, Arrays.asList((Directions.Step) null));
	}

	@Test
	public void testDirectionsEqualsHashCode() {
		Directions d1 = new Directions(startAddress, endAddress, steps);
		Directions d2 = new Directions(startAddress, endAddress, steps);
		
		assertThat(d1, equalTo(d2));
		assertThat(d2, equalTo(d1));
		assertThat(d1.hashCode(), equalTo(d2.hashCode()));
		
		Directions d3 = new Directions("0 Foo Way", endAddress, steps);
		assertThat(d3, not(equalTo(d1)));
		
		Directions d4 = new Directions(startAddress, "0 Foo Way", steps);
		assertThat(d4, not(equalTo(d1)));
		
		List<Step> steps = new ArrayList<>(this.steps);
		steps.remove(1);
		
		Directions d5 = new Directions(startAddress, endAddress, steps);
		assertThat(d5, not(equalTo(d1)));
	}

	//-- Step -- Tests --
	
	String instructions = "instructions";
	Location start = new Location(1.0, 1.0);
	Location end = new Location(1.2, 1.2);
		
	@Test
	public void testStep() {
		Step step = new Step(instructions, start, end);
		assertThat(step.instructions, equalTo(instructions));
		assertThat(step.start, equalTo(start));
		assertThat(step.end, equalTo(end));
	}
	
	// Invalid parameters
	
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
