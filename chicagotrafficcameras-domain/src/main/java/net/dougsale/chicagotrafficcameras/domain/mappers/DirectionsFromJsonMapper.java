/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain.mappers;

import static org.apache.commons.lang3.Validate.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.dougsale.chicagotrafficcameras.Directions;
import net.dougsale.chicagotrafficcameras.domain.Location;

/**
 * DirectionsFromJsonMapper creates an immutable Directions object from Google's JSON representation.
 * <P>
 * Uses Jackson under the hood.  As Jackson requires either publicly available fields,
 * publicly available setter methods, or annotations to inform object construction,
 * DirectionsFromJsonMapper serves to construct the immutable Directions object without polluting the
 * Directions domain with Jackson annotations. 
 * </P>
 * @author dsale
 *
 */
public class DirectionsFromJsonMapper {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	private JsonPointer startAddressPointer = JsonPointer.valueOf("/startAddress");
	private JsonPointer endAddressPointer = JsonPointer.valueOf("/endAddress");
	private JsonPointer stepsPointer = JsonPointer.valueOf("/steps");
	private JsonPointer instructionsPointer = JsonPointer.valueOf("/instructions");
	private JsonPointer startLatitudePointer = JsonPointer.valueOf("/start/latitude");
	private JsonPointer startLongitudePointer = JsonPointer.valueOf("/start/longitude");
	private JsonPointer endLatitudePointer = JsonPointer.valueOf("/end/latitude");
	private JsonPointer endLongitudePointer = JsonPointer.valueOf("/end/longitude");
	
	public Directions map(String json) throws JsonParseException, JsonMappingException, IOException {
		notNull(json, "invalid parameter: json=" + json);
		notEmpty(json.trim(), "invalid parameter: json=" + json);
		
		JsonNode rootNode = objectMapper.readTree(json);
		JsonNode stepsNode = rootNode.at(stepsPointer);

		List<Directions.Step> steps = new ArrayList<Directions.Step>(); 
		Directions.Step step;
		String instructions;
		Double startLatitude, startLongitude, endLatitude, endLongitude;

		// process steps
		for (JsonNode stepNode : stepsNode) {
			instructions = stepNode.at(instructionsPointer).asText();
			startLatitude = stepNode.at(startLatitudePointer).asDouble();
			startLongitude = stepNode.at(startLongitudePointer).asDouble();
			endLatitude = stepNode.at(endLatitudePointer).asDouble();
			endLongitude = stepNode.at(endLongitudePointer).asDouble();

			// build step
			step = new Directions.Step(
					instructions,
					new Location(startLatitude, startLongitude),
					new Location(endLatitude, endLongitude)
				);

			steps.add(step);
		}
		
		
		String startAddress = rootNode.at(startAddressPointer).asText();
		String endAddress = rootNode.at(endAddressPointer).asText();
		Directions directions = new Directions(startAddress, endAddress, steps);
		
		return directions;
	}
}
