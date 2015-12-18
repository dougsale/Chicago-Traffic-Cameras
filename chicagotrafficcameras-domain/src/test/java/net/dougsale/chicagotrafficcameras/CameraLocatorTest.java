/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Approach;
import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;
import net.dougsale.chicagotrafficcameras.repository.CamerasRepository;

public class CameraLocatorTest {

	CameraLocator locator;
	
//	@BeforeClass
//	void init() throws ClassNotFoundException, IOException {
//	}
//	
	@Test
	public void testLocateCamerasOnDiagonalRoute() throws ClassNotFoundException, IOException {
		// shows the limitations of locating using bounding box only:
		// box is big, leads to false positives
		
		// trying to correct by calculating minimum distance from camera to line segment
		// originally, 5 cameras show up with bounding box, only 2 are valid:  those with addresses on Ogden
		// one has the smallest distance, the other the largest
		// i'm not sure if this is an artifact of the calculation, i've tried scaling the arguments and the result
		// as you can see the results are the same for multiple scaling values
		// i think i should try the calculations - a little test program - using BigDecimal with the original text
		// values from the CSV (Cameras, Chicago) and the .js (start and end line segment, google)
		// if that still gives erroneous results, it's and issue with the coding of the cameras, likely (thx, Chicago)
		// in such a case, only matching by street name will work (i'm afraid that has it's own pitfalls...)
		// street name is easy to implement, though (just concerned about multiple names for streets: Cicero/Mandela, North Ave, rte 64, etc)
		
//		UNSCALED: 0.0015802  *** net.dougsale.chicagotrafficcameras.domain.RedLightCamera@446cdf90[location=net.dougsale.chicagotrafficcameras.domain.Location@799f7e29[latitude=41.881454,longitude=-87.666802],approaches=[NORTHBOUND, WESTBOUND],intersection=[Madison, Ashland]]
//		SCALED:   0.0015802  *** net.dougsale.chicagotrafficcameras.domain.RedLightCamera@446cdf90[location=net.dougsale.chicagotrafficcameras.domain.Location@799f7e29[latitude=41.881454,longitude=-87.666802],approaches=[NORTHBOUND, WESTBOUND],intersection=[Madison, Ashland]]
//		UNSCALED: 0.0022642  *** net.dougsale.chicagotrafficcameras.domain.SpeedCamera@5c29bfd[location=net.dougsale.chicagotrafficcameras.domain.Location@7aec35a[latitude=41.883191521391076,longitude=-87.66411469955516],approaches=[NORTHBOUND, SOUTHBOUND],address=115 N Ogden]
//		SCALED:   0.0022642  *** net.dougsale.chicagotrafficcameras.domain.SpeedCamera@5c29bfd[location=net.dougsale.chicagotrafficcameras.domain.Location@7aec35a[latitude=41.883191521391076,longitude=-87.66411469955516],approaches=[NORTHBOUND, SOUTHBOUND],address=115 N Ogden]
//		UNSCALED: 0.0007455  *** net.dougsale.chicagotrafficcameras.domain.SpeedCamera@42110406[location=net.dougsale.chicagotrafficcameras.domain.Location@531d72ca[latitude=41.88451419700416,longitude=-87.66704209415728],approaches=[SOUTHBOUND],address=140 N Ashland]
//		SCALED:   0.0007455  *** net.dougsale.chicagotrafficcameras.domain.SpeedCamera@42110406[location=net.dougsale.chicagotrafficcameras.domain.Location@531d72ca[latitude=41.88451419700416,longitude=-87.66704209415728],approaches=[SOUTHBOUND],address=140 N Ashland]
//		UNSCALED: 0.0005393  *** net.dougsale.chicagotrafficcameras.domain.SpeedCamera@22d8cfe0[location=net.dougsale.chicagotrafficcameras.domain.Location@579bb367[latitude=41.88451740794813,longitude=-87.66674834697315],approaches=[NORTHBOUND],address=141 N Ashland]
//		SCALED:   0.0005393  *** net.dougsale.chicagotrafficcameras.domain.SpeedCamera@22d8cfe0[location=net.dougsale.chicagotrafficcameras.domain.Location@579bb367[latitude=41.88451740794813,longitude=-87.66674834697315],approaches=[NORTHBOUND],address=141 N Ashland]
//		UNSCALED: 0.0000161  *** net.dougsale.chicagotrafficcameras.domain.SpeedCamera@1de0aca6[location=net.dougsale.chicagotrafficcameras.domain.Location@255316f2[latitude=41.86040786445197,longitude=-87.69867209877323],approaches=[EASTBOUND, WESTBOUND],address=2900 W Ogden Ave]
//		SCALED:   0.0000161  *** net.dougsale.chicagotrafficcameras.domain.SpeedCamera@1de0aca6[location=net.dougsale.chicagotrafficcameras.domain.Location@255316f2[latitude=41.86040786445197,longitude=-87.69867209877323],approaches=[EASTBOUND, WESTBOUND],address=2900 W Ogden Ave]


		locator = new CameraLocator(new CamerasRepository("Cameras.ser").getCameras());

		String startAddress = "615 N Ogden Ave, Chicago, IL 60642, USA";
		String endAddress = "3315 W Ogden Ave, Chicago, IL 60623, USA";
		List<Directions.Step> steps =
			Arrays.asList(
				new Directions.Step(
					"Head <b>northeast</b> on <b>N Ogden Ave</b> toward <b>W Erie St</b>",
					new Location(41.8929664, -87.65731360000001),
					new Location(41.8949373, -87.65565119999997)
				),
				new Directions.Step(
					"Make a <b>U-turn</b> at <b>W Superior St</b>",
					new Location(41.8949373, -87.65565119999997),
					new Location(41.8666981, -87.6836672)
				),
				new Directions.Step(
					"Keep <b>left</b> to continue on <b>Historic U.S. 66 W</b>/<b>W Ogden Ave</b>",
					new Location(41.8666981, -87.6836672),
					new Location(41.8630448, -87.69086219999997)
				),
				new Directions.Step(
					"Keep <b>right</b> to continue on <b>W Ogden Ave</b>",
					new Location(41.8630448, -87.69086219999997),
					new Location(41.8618937, -87.6944863)
				),
				new Directions.Step(
					"Keep <b>left</b> to continue on <b>Historic U.S. 66 W</b>/<b>W Ogden Ave</b>",
					new Location(41.8618937, -87.6944863),
					new Location(41.8571103, -87.7078085)
				),
				new Directions.Step(
					"Keep <b>right</b> to continue on <b>W Ogden Ave</b>",
					new Location(41.8571103, -87.7078085),
					new Location(41.8567775, -87.7090442)
				),
				new Directions.Step(
					"Make a <b>U-turn</b> at <b>S Christiana Ave</b><div style=\"font-size:0.9em\">Destination will be on the right</div>",
					new Location(41.8567775, -87.7090442),
					new Location(41.8566225, -87.70858179999999)
				)
			);
		
		Directions directions = new Directions(startAddress, endAddress, steps);
		Cameras located = locator.locate(directions);
		
		// should be no red light cameras.  with only bounding box filter these also match: Madison & Ashland red light camera
		assertThat(located.getTypes(), equalTo(Collections.singleton(SpeedCamera.class)));
		
		Set<SpeedCamera> cameras = located.get(SpeedCamera.class);
		// should be only 2 speed cameras.  with only bounding box filter these also match: 140 & 141 N Ashland
		assertThat(cameras.size(), equalTo(2));

		Iterator<SpeedCamera> iter = cameras.iterator();
		SpeedCamera camera1 = iter.next();
		SpeedCamera camera2 = iter.next();
		
		assertThat(
			(camera1.address.equals("2900 W Ogden Ave") && camera2.address.equals("115 N Ogden")) ||
			(camera1.address.equals("115 N Ogden") && camera2.address.equals("2900 W Ogden Ave")),
			is(true)
		);
	}
}
