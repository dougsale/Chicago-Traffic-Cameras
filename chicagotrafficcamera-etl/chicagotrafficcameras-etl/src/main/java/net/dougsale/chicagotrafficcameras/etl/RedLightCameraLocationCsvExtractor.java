package net.dougsale.chicagotrafficcameras.etl;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.dougsale.chicagotrafficcameras.domain.Approach;
import net.dougsale.chicagotrafficcameras.domain.RedLightCameraLocation;

public class RedLightCameraLocationCsvExtractor {
	
	private ArrayList<Approach> approaches = new ArrayList<>(3);
	
	public List<RedLightCameraLocation> extract(Reader source) {

		ArrayList<RedLightCameraLocation> locations = new ArrayList<>(150);
		
		Scanner scanner = new Scanner(source);

		// discard column titles
		if (scanner.hasNextLine())
			scanner.nextLine();

		// make scanner.next() returns values by column (CSV)
		scanner.useDelimiter(",");

//		System.out.println("var redLightCameras = [");
		while (scanner.hasNext()) {

			String[] intersection = extractIntersection(scanner);
			Approach[] approaches = extractApproaches(scanner);

			// ignore "go live date"
			scanner.next();

			double latitude = scanner.nextDouble();
			double longitude = scanner.nextDouble();

			// ignore "location"
			scanner.nextLine();

			RedLightCameraLocation location =
					new RedLightCameraLocation(intersection, latitude, longitude, approaches);
			locations.add(location);
			
			System.out.println(location);
//					"\t{" +
//					"\n\t\tintersection: " + intersection +
//					",\n\t\tapproach: " + approaches +
//					",\n\t\tlatitude: " + latitude +
//					",\n\t\tlongitude: " + longitude +
//					"\n\t},"
//				);
		}
//		System.out.println("];");

		scanner.close();
		return locations;
	}

	private String[] extractIntersection(Scanner scanner) {
		String intersection = scanner.next();
		intersection = intersection.replace('?', ' ');
		return intersection.split("-");
	}

	private Approach[] extractApproaches(Scanner scanner) {
		approaches.clear();

		for (int i = 0; i < 3; i++) {
			String approach = scanner.next();
			switch (approach) {
			case "NB":
				approaches.add(Approach.NORTHBOUND);
				break;
			case "NEB":
				approaches.add(Approach.NORTHEASTBOUND);
				break;
			case "NWB":
				approaches.add(Approach.NORTHWESTBOUND);
				break;
			case "SB":
				approaches.add(Approach.SOUTHBOUND);
				break;
			case "SEB":
				approaches.add(Approach.SOUTHEASTBOUND);
				break;
			case "SWB":
				approaches.add(Approach.SOUTHWESTBOUND);
				break;
			case "EB":
				approaches.add(Approach.EASTBOUND);
				break;
			case "WB":
				approaches.add(Approach.WESTBOUND);
				break;
			case "":
				// okay
				break;
			default:
				System.err.println("Unhandled approach: " + approach);
				break;
			}
		}

		return approaches.toArray(new Approach[approaches.size()]);
	}

}
