package net.dougsale.chicagotrafficcameras;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.mappers.CamerasFromJsonMapper;
import net.dougsale.chicagotrafficcameras.domain.mappers.MapperException;
import net.dougsale.chicagotrafficcameras.domain.mappers.RouteFromJsonMapper;
import net.dougsale.chicagotrafficcameras.repository.RepositoryException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/ctc-context.xml")
public class CameraServiceIT {

	@Autowired
	CameraService cameraService;
	
	RouteFromJsonMapper routeFromJson = new RouteFromJsonMapper();
	CamerasFromJsonMapper camerasFromJson = new CamerasFromJsonMapper();
	
	@Test
	public void _615_N_Ogden_Ave_3315_W_Ogden_Ave() throws RepositoryException, MapperException {
		String routeJson = "{\"startAddress\":\"615 N Ogden Ave, Chicago, IL 60642, USA\",\"endAddress\":\"3315 W Ogden Ave, Chicago, IL 60623, USA\",\"steps\":[{\"instructions\":\"Head <b>northeast</b> on <b>N Ogden Ave</b> toward <b>W Erie St</b>\",\"start\":{\"latitude\":41.8929664,\"longitude\":-87.65731360000001},\"end\":{\"latitude\":41.8949373,\"longitude\":-87.65565119999997}},{\"instructions\":\"Make a <b>U-turn</b> at <b>W Superior St</b>\",\"start\":{\"latitude\":41.8949373,\"longitude\":-87.65565119999997},\"end\":{\"latitude\":41.8666981,\"longitude\":-87.6836672}},{\"instructions\":\"Keep <b>left</b> to continue on <b>Historic U.S. 66 W</b>/<b>W Ogden Ave</b>\",\"start\":{\"latitude\":41.8666981,\"longitude\":-87.6836672},\"end\":{\"latitude\":41.8571103,\"longitude\":-87.7078085}},{\"instructions\":\"Keep <b>right</b> to continue on <b>W Ogden Ave</b>\",\"start\":{\"latitude\":41.8571103,\"longitude\":-87.7078085},\"end\":{\"latitude\":41.8567775,\"longitude\":-87.7090442}},{\"instructions\":\"Make a <b>U-turn</b> at <b>S Christiana Ave</b><div style=\\\"font-size:0.9em\\\">Destination will be on the right</div>\",\"start\":{\"latitude\":41.8567775,\"longitude\":-87.7090442},\"end\":{\"latitude\":41.8566225,\"longitude\":-87.70858179999999}}]}";
		Route route = routeFromJson.map(routeJson);
		String camerasJson = "{\"trafficCameras\":{\"redLight\":[],\"speed\":[{\"latitude\":41.86040786445197,\"longitude\":-87.69867209877323,\"address\":\"2900 W Ogden Ave\",\"approaches\":[\"EASTBOUND\",\"WESTBOUND\"]},{\"latitude\":41.883191521391076,\"longitude\":-87.66411469955516,\"address\":\"115 N Ogden\",\"approaches\":[\"NORTHBOUND\",\"SOUTHBOUND\"]}]}}";
		Cameras expected = camerasFromJson.map(camerasJson);
		Cameras actual = cameraService.getCameras(route);
		assertThat(actual, equalTo(expected));
	}
	
}