/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain.mappers;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Direction;
import net.dougsale.chicagotrafficcameras.domain.builders.RedLightCameraBuilder;
import net.dougsale.chicagotrafficcameras.domain.builders.SpeedCameraBuilder;

public class CamerasFromJsonMapper {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private JsonPointer redLightCamerasPointer = JsonPointer.valueOf("/trafficCameras/redLight");
	private JsonPointer speedCamerasPointer = JsonPointer.valueOf("/trafficCameras/speed");
	private JsonPointer latitudePointer = JsonPointer.valueOf("/latitude");
	private JsonPointer longitudePointer = JsonPointer.valueOf("/longitude");
	private JsonPointer approachesPointer = JsonPointer.valueOf("/approaches");
	private JsonPointer intersectionPointer = JsonPointer.valueOf("/intersection");
	private JsonPointer addressPointer = JsonPointer.valueOf("/address");

	public Cameras map(String json) throws JsonParseException, JsonMappingException, IOException {
		notNull(json, "invalid parameter: json=" + json);
		notEmpty(json.trim(), "invalid parameter: json=" + json);
		
		JsonNode rootNode = objectMapper.readTree(json);
		Cameras cameras = new Cameras();
		
		{
			JsonNode camerasNode = rootNode.at(redLightCamerasPointer);
			RedLightCameraBuilder builder = new RedLightCameraBuilder();
			
			for (JsonNode cameraNode : camerasNode) {
				builder.withLocation(cameraNode.at(latitudePointer).asDouble(), cameraNode.at(longitudePointer).asDouble());
			
				for (JsonNode streetNode : cameraNode.at(intersectionPointer))
					builder.withStreet(streetNode.asText());
				
				for (JsonNode approachNode : cameraNode.at(approachesPointer))
					builder.withApproach(fromString(approachNode.asText()));
				
				cameras.add(builder.build());
				builder.reset();
			}
		}
		{
			JsonNode camerasNode = rootNode.at(speedCamerasPointer);
			SpeedCameraBuilder builder = new SpeedCameraBuilder();

			for (JsonNode cameraNode : camerasNode) {
				builder.withLocation(cameraNode.at(latitudePointer).asDouble(), cameraNode.at(longitudePointer).asDouble());
				
				builder.withAddress(cameraNode.at(addressPointer).asText());

				for (JsonNode approachNode : cameraNode.at(approachesPointer))
					builder.withApproach(fromString(approachNode.asText()));
				
				cameras.add(builder.build());
				builder.reset();
			}
		}
		
		return cameras;
	}
	
	// move this stuff into enum
	private Direction fromString(String direction) {
		Direction d = null;
		
		for (int i = 0; i < 3; i++) {
			switch (direction) {
			case "NORTHBOUND":
				d  = (Direction.NORTHBOUND);
				break;
			case "NORTHEASTBOUND":
				d  = (Direction.NORTHEASTBOUND);
				break;
			case "NORTHWESTBOUND":
				d  = (Direction.NORTHWESTBOUND);
				break;
			case "SOUTHBOUND":
				d  = (Direction.SOUTHBOUND);
				break;
			case "SOUTHEASTBOUND":
				d  = (Direction.SOUTHEASTBOUND);
				break;
			case "SOUTHWESTBOUND":
				d  = (Direction.SOUTHWESTBOUND);
				break;
			case "EASTBOUND":
				d  = (Direction.EASTBOUND);
				break;
			case "WESTBOUND":
				d  = (Direction.WESTBOUND);
				break;
			case "":
				throw new IllegalArgumentException("Unhandled approach: " + direction);
			default:
				throw new IllegalArgumentException("Unhandled approach: " + direction);
			}
		}
		
		return d;
	}

}
