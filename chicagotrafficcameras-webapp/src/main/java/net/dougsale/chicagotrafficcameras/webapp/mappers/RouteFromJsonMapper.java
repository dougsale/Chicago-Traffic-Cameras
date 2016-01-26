/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.webapp.mappers;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.domain.builders.RouteBuilder;

/**
 * RouteFromJsonMapper creates an immutable Route object from a JSON representation.
 * <P>
 * Uses Jackson under the hood.  As Jackson requires either publicly available fields,
 * publicly available setter methods, or annotations to inform object construction,
 * RouteFromJsonMapper serves to construct the immutable Route object without polluting the
 * Route domain with Jackson annotations. 
 * </P>
 * @author dsale
 *
 */
public class RouteFromJsonMapper {

	private ObjectMapper objectMapper = new ObjectMapper();
	private JsonPointer startAddressPointer = JsonPointer.valueOf("/startAddress");
	private JsonPointer endAddressPointer = JsonPointer.valueOf("/endAddress");
	private JsonPointer stepsPointer = JsonPointer.valueOf("/steps");
	private JsonPointer instructionsPointer = JsonPointer.valueOf("/instructions");
	private JsonPointer startLatitudePointer = JsonPointer.valueOf("/start/latitude");
	private JsonPointer startLongitudePointer = JsonPointer.valueOf("/start/longitude");
	private JsonPointer endLatitudePointer = JsonPointer.valueOf("/end/latitude");
	private JsonPointer endLongitudePointer = JsonPointer.valueOf("/end/longitude");
	
	public Route map(String json) throws MapperException {
		notNull(json, "invalid parameter: json=" + json);
		notEmpty(json.trim(), "invalid parameter: json=" + json);

		RouteBuilder builder = new RouteBuilder();
		
		try {
			JsonNode rootNode = objectMapper.readTree(json);
			JsonNode stepsNode = rootNode.at(stepsPointer);
	
			builder.withStartAddress(rootNode.at(startAddressPointer).asText());
			builder.withEndAddress(rootNode.at(endAddressPointer).asText());

			for (JsonNode stepNode : stepsNode) {
				builder.withStep(
					stepNode.at(instructionsPointer).asText(),
					stepNode.at(startLatitudePointer).asDouble(),
					stepNode.at(startLongitudePointer).asDouble(),
					stepNode.at(endLatitudePointer).asDouble(),
					stepNode.at(endLongitudePointer).asDouble()
				);
			}
					
		} catch (JsonParseException e) {
			throw new MapperException(MapperErrorCodes.MALFORMED_JSON, e).withContext("routeJson", json);
		} catch (JsonProcessingException e) {
			throw new MapperException(MapperErrorCodes.JSON_PROCESSING, e).withContext("routeJson", json);
		} catch (IOException e) {
			throw new MapperException(MapperErrorCodes.GENERIC_IO, e).withContext("routeJson", json);
		}
		
		return builder.build();
	}
}
