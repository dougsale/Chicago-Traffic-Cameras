package net.dougsale.chicagotrafficcameras.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.builders.RouteBuilder;
import net.dougsale.chicagotrafficcameras.domain.builders.SpeedCameraBuilder;
import net.dougsale.chicagotrafficcameras.repository.RepositoryException;
import net.dougsale.chicagotrafficcameras.services.CameraService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/ctc-context.xml")
public class CameraServiceIT {

	@Autowired
	CameraService cameraService;
	
	@Test
	public void _615_N_Ogden_Ave_3315_W_Ogden_Ave() throws RepositoryException {
		Route route = new RouteBuilder()
			.withStartAddress("615 N Ogden Ave, Chicago, IL 60642, USA")
			.withEndAddress("3315 W Ogden Ave, Chicago, IL 60623, USA")
			.withStep("Head <b>northeast</b> on <b>N Ogden Ave</b> toward <b>W Erie St</b>", 41.8929664, -87.65731360000001, 41.8949373, -87.65565119999997)
			.withStep("Make a <b>U-turn</b> at <b>W Superior St</b>", 41.8949373, -87.65565119999997, 41.8666981, -87.6836672)
			.withStep("Keep <b>left</b> to continue on <b>Historic U.S. 66 W</b>/<b>W Ogden Ave</b>", 41.8666981, -87.6836672, 41.8571103, -87.7078085)
			.withStep("Keep <b>right</b> to continue on <b>W Ogden Ave</b>", 41.8571103, -87.7078085, 41.8567775, -87.7090442)
			.withStep("Make a <b>U-turn</b> at <b>S Christiana Ave</b><div style=\"font-size:0.9em\">Destination will be on the right</div>", 41.8567775, -87.7090442, 41.8566225, -87.70858179999999)
			.build();
				
		Cameras expected = new Cameras();
		expected.add(
			new SpeedCameraBuilder()
				.withLocation(41.86040786445197, -87.69867209877323)
				.withAddress("2900 W Ogden Ave")
				.withApproach("EASTBOUND")
				.withApproach("WESTBOUND")
			.build());
		expected.add(
				new SpeedCameraBuilder()
					.withLocation(41.883191521391076, -87.66411469955516)
					.withAddress("115 N Ogden")
					.withApproach("NORTHBOUND")
					.withApproach("SOUTHBOUND")
				.build());
		
		Cameras actual = cameraService.getCameras(route);
		assertThat(actual, equalTo(expected));
	}
	
}