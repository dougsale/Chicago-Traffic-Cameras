package net.dougsale.chicagotrafficcameras.etl;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.dougsale.chicagotrafficcameras.domain.Approach;
import net.dougsale.chicagotrafficcameras.domain.SpeedCameraLocation;

public class SpeedCameraLocationToJsonTransformer {
	
	public static void main(String[] args) {
		
//		String path = "/home/dsale/dev/data/Chicago Data/Red Light Camera Locations/TestLocations.csv";
		String path = "/home/dsale/dev/data/Chicago Data/Speed Camera Locations/Speed_Camera_Locations.csv";

		try (Reader source = new FileReader(path)) {
			List<SpeedCameraLocation> locations = new SpeedCameraLocationCsvExtractor().extract(source);
			SpeedCameraLocationToJsonTransformer transformer = new SpeedCameraLocationToJsonTransformer(false);
			
//			for (SpeedCameraLocation l : locations) {
//				System.out.println(transformer.transform(l));
//			}
			System.out.println(transformer.transform(locations));
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}		
	}
	
	private String space;
	private String tab;
	private String newline;
	
	public SpeedCameraLocationToJsonTransformer(boolean pretty) {
		space = pretty? " " : "";
		tab = pretty? "\t" : "";
		newline = pretty? System.getProperty("line.separator", "\n") : "";
	}
	
	public String transform(SpeedCameraLocation location) {
		StringBuilder builder = new StringBuilder();
		transform(location, builder);
		return builder.toString();
	}
	
	public String transform(Collection<SpeedCameraLocation> locations) {
		// pretty or not, always separating rows
		String localNewline = System.getProperty("line.separator", "\n");

//		this.newline = "\n\t\t";
		
		StringBuilder builder = new StringBuilder();
		builder.append("{").append(localNewline).append(tab).append("locations:").append(space).append("[").append(localNewline);
		
		for (Iterator<SpeedCameraLocation> iterator = locations.iterator(); iterator.hasNext();) {
			transform(iterator.next(), builder);
			if (iterator.hasNext())
				builder.append(",").append(localNewline);
			else
				builder.append(localNewline);
		}

		builder.append(tab).append("]").append(localNewline).append("}").append(localNewline);
		
		return builder.toString();
	}

	private void transform(SpeedCameraLocation location, StringBuilder builder) {
		builder.append("{").append(newline);
		
		builder.append(tab).append("latitude:").append(space).append(location.getLatitude()).append(",").append(newline);
		builder.append(tab).append("longitude:").append(space).append(location.getLongitude()).append(",").append(newline);

		builder.append(tab).append("address:").append(space).append("\"").append(location.getAddress()).append("\"").append(",").append(newline);

		builder.append(tab).append("approaches:").append(space).append("[").append(space);
		Approach[] approaches = location.getApproaches();
		for (int i = 0; i < approaches.length; i++) {
			builder.append("\"").append(approaches[i]).append("\"");
			if (i + 1 < approaches.length) builder.append(",").append(space);
		}
		builder.append(space).append("]").append(newline);
		builder.append("}");
	}
}
