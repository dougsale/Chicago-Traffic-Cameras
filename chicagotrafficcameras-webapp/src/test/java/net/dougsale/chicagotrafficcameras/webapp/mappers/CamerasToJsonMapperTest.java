package net.dougsale.chicagotrafficcameras.webapp.mappers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.TreeSet;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Direction;
import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

public class CamerasToJsonMapperTest {

	RedLightCamera rlCamera_1 = new RedLightCamera(
			new TreeSet<String>(Arrays.asList("Milwaukee", "North", "Damen")),
			new Location(-68.132412, 104.345612),
			EnumSet.of(Direction.EASTBOUND, Direction.SOUTHBOUND)
		);
	
	RedLightCamera rlCamera_2 = new RedLightCamera(
			new TreeSet<String>(Arrays.asList("Western", "North")),
			new Location(-67.156712, 103.312312),
			EnumSet.of(Direction.NORTHBOUND, Direction.SOUTHBOUND)
		);
	
	SpeedCamera spCamera_1 = new SpeedCamera(
			"3312 W Fullerton Ave",
			new Location(-68.132412, 104.345612),
			EnumSet.of(Direction.EASTBOUND, Direction.WESTBOUND)
		);
	
	SpeedCamera spCamera_2 = new SpeedCamera(
			"6123 N Cicero Ave",
			new Location(-67.156712, 103.312312),
			EnumSet.of(Direction.NORTHBOUND, Direction.SOUTHBOUND)
		);
	
	CamerasToJsonMapper camerasToJson = new CamerasToJsonMapper();
	
	//TESTS
	
	@Test
	public void testSerializeEmptyCameras() throws MapperException {
		Cameras cameras = new Cameras();
		assertThat(camerasToJson.map(cameras),
				equalTo("{\"redLightCameras\":[],\"speedCameras\":[]}"));
	}

	@Test
	public void testSerializeOneRedLightCameraCameras() throws MapperException {
		Cameras cameras = new Cameras();
		cameras.add(rlCamera_1);
		assertThat(camerasToJson.map(cameras),
				equalTo("{\"redLightCameras\":[{\"latitude\":-68.132412,\"longitude\":104.345612,\"intersection\":[\"Milwaukee\",\"Damen\",\"North\"],\"approaches\":[\"SOUTHBOUND\",\"EASTBOUND\"]}],\"speedCameras\":[]}"));
	}

	@Test
	public void testSerializeOneSpeedCameraCameras() throws MapperException {
		Cameras cameras = new Cameras();
		cameras.add(spCamera_1);
		assertThat(camerasToJson.map(cameras),
				equalTo("{\"redLightCameras\":[],\"speedCameras\":[{\"latitude\":-68.132412,\"longitude\":104.345612,\"address\":\"3312 W Fullerton Ave\",\"approaches\":[\"EASTBOUND\",\"WESTBOUND\"]}]}"));
	}

	@Test
	public void testSerializeCameras() throws MapperException {
		Cameras cameras = new Cameras();
		cameras.add(rlCamera_1);
		cameras.add(spCamera_1);
		cameras.add(rlCamera_2);
		cameras.add(spCamera_2);
		assertThat(camerasToJson.map(cameras),
				equalTo("{\"redLightCameras\":[{\"latitude\":-68.132412,\"longitude\":104.345612,\"intersection\":[\"Milwaukee\",\"Damen\",\"North\"],\"approaches\":[\"SOUTHBOUND\",\"EASTBOUND\"]},{\"latitude\":-67.156712,\"longitude\":103.312312,\"intersection\":[\"Western\",\"North\"],\"approaches\":[\"NORTHBOUND\",\"SOUTHBOUND\"]}],\"speedCameras\":[{\"latitude\":-68.132412,\"longitude\":104.345612,\"address\":\"3312 W Fullerton Ave\",\"approaches\":[\"EASTBOUND\",\"WESTBOUND\"]},{\"latitude\":-67.156712,\"longitude\":103.312312,\"address\":\"6123 N Cicero Ave\",\"approaches\":[\"NORTHBOUND\",\"SOUTHBOUND\"]}]}"));
	}
}