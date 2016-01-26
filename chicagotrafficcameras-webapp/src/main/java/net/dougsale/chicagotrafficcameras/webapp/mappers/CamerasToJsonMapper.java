/**
 * 
 */
package net.dougsale.chicagotrafficcameras.webapp.mappers;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Direction;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

/**
 * Maps Cameras object to a custom JSON representation.
 * @author dsale
 *
 */
public class CamerasToJsonMapper {
	
	private final ObjectMapper mapper;

	public CamerasToJsonMapper() {
		mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(Cameras.class, new CamerasSerializer());
		mapper.registerModule(module);
	}
	
	public String map(Cameras cameras) throws MapperException {
		StringWriter writer = new StringWriter();

		try {
			mapper.writeValue(writer, cameras);
		} catch (JsonProcessingException e) {
			throw new MapperException(MapperErrorCodes.JSON_PROCESSING, e).withContext("cameras", cameras);
		} catch (IOException e) {
			throw new MapperException(MapperErrorCodes.GENERIC_IO, e).withContext("cameras", cameras);
		}

		return writer.toString();
	}
	
	private class CamerasSerializer extends JsonSerializer<Cameras> {

		@Override
		public void serialize(Cameras cameras, JsonGenerator generator, SerializerProvider provider)
				throws IOException, JsonProcessingException {
//			generator.useDefaultPrettyPrinter();
			
			generator.writeStartObject();
			
			generator.writeArrayFieldStart("redLightCameras");
			for (RedLightCamera camera : cameras.get(RedLightCamera.class, Cameras.BY_LATITUDE)) {
				generator.writeStartObject();
				generator.writeNumberField("latitude", camera.getLocation().latitude);
				generator.writeNumberField("longitude", camera.getLocation().longitude);
				generator.writeArrayFieldStart("intersection");
				for (String street : camera.getIntersection())
					generator.writeString(street);
				generator.writeEndArray();
				generator.writeArrayFieldStart("approaches");
				for (Direction approach : camera.getApproaches())
					generator.writeString(approach.toString());
				generator.writeEndArray();
				generator.writeEndObject();
			}
			generator.writeEndArray();

			generator.writeArrayFieldStart("speedCameras");
			for (SpeedCamera camera : cameras.get(SpeedCamera.class, Cameras.BY_LATITUDE)) {
				generator.writeStartObject();
				generator.writeNumberField("latitude", camera.getLocation().latitude);
				generator.writeNumberField("longitude", camera.getLocation().longitude);
				generator.writeStringField("address", camera.address);
				generator.writeArrayFieldStart("approaches");
				for (Direction approach : camera.getApproaches())
					generator.writeString(approach.toString());
				generator.writeEndArray();
				generator.writeEndObject();
			}
			generator.writeEndArray();

			generator.writeEndObject();
		}
		
	}
}
