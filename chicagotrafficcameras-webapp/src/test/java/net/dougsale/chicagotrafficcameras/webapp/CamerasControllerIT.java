package net.dougsale.chicagotrafficcameras.webapp;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import net.dougsale.chicagotrafficcameras.repository.RepositoryException;
import net.dougsale.chicagotrafficcameras.webapp.mappers.MapperException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/ctc-context.xml", "/ctc-webapp-context.xml" })
public class CamerasControllerIT {

	@Autowired
	CamerasController camerasController;

	@Test
	public void _615_N_Ogden_Ave_3315_W_Ogden_Ave() throws JsonParseException, JsonMappingException, IOException, RepositoryException, MapperException {
		String route = "{\"startAddress\":\"201 N Columbus Dr, Chicago, IL 60611, USA\",\"endAddress\":\"400 E Illinois St, Chicago, IL 60611, USA\",\"steps\":[{\"instructions\":\"Head <b>south</b> on <b>N Columbus Dr</b> toward <b>E Lower Wacker Dr</b>\",\"start\":{\"latitude\":41.8880992,\"longitude\":-87.6207005},\"end\":{\"latitude\":41.88776319999999,\"longitude\":-87.62070829999999}},{\"instructions\":\"Make a <b>U-turn</b> at <b>E Lower Wacker Dr</b>\",\"start\":{\"latitude\":41.88776319999999,\"longitude\":-87.62070829999999},\"end\":{\"latitude\":41.8910117,\"longitude\":-87.62012340000001}},{\"instructions\":\"Turn <b>right</b> onto <b>E Illinois St</b><div style=\\\"font-size:0.9em\\\">Destination will be on the left</div>\",\"start\":{\"latitude\":41.8910117,\"longitude\":-87.62012340000001},\"end\":{\"latitude\":41.8910524,\"longitude\":-87.6178458}}]}";
		String cameras = "{\"redLightCameras\":[{\"latitude\":41.891002,\"longitude\":-87.620224,\"intersection\":[\"Columbus\",\"Illinois\"],\"approaches\":[\"NORTHBOUND\",\"SOUTHBOUND\"]}],\"speedCameras\":[{\"latitude\":41.89003775504898,\"longitude\":-87.62017903427099,\"address\":\"449 N Columbus Dr\",\"approaches\":[\"NORTHBOUND\"]},{\"latitude\":41.890122352505166,\"longitude\":-87.62041639513696,\"address\":\"450 N Columbus Dr\",\"approaches\":[\"SOUTHBOUND\"]},{\"latitude\":41.89091009572781,\"longitude\":-87.61934752145963,\"address\":\"319 E Illinois St\",\"approaches\":[\"EASTBOUND\"]}]}";
		assertThat(camerasController.getCameras(route), equalTo(cameras));
	}
}