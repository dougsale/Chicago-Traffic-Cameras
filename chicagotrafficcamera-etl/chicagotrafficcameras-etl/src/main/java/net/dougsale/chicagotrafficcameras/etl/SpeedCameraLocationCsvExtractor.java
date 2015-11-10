package net.dougsale.chicagotrafficcameras.etl;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.dougsale.chicagotrafficcameras.domain.Approach;
import net.dougsale.chicagotrafficcameras.domain.SpeedCameraLocation;

public class SpeedCameraLocationCsvExtractor {
	
	private ArrayList<Approach> approaches = new ArrayList<>(2);
	
	public List<SpeedCameraLocation> extract(Reader source) {

		ArrayList<SpeedCameraLocation> locations = new ArrayList<>(150);
		
		Scanner scanner = new Scanner(source);

		// discard column titles
		if (scanner.hasNextLine())
			scanner.nextLine();

		// make scanner.next() returns values by column (CSV)
		scanner.useDelimiter(",");

//		System.out.println("var SpeedCameras = [");
		while (scanner.hasNext()) {

			String address = extractAddress(scanner);
			Approach[] approaches = extractApproaches(scanner);

			// ignore "go live date"
			scanner.next();

			double latitude = scanner.nextDouble();
			double longitude = scanner.nextDouble();

			// ignore "location"
			scanner.nextLine();

			SpeedCameraLocation location =
					new SpeedCameraLocation(address, latitude, longitude, approaches);
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

	private String extractAddress(Scanner scanner) {
		String address = scanner.next();
		address = address.replace(" (Speed Camera)", "");
		return address;
	}

	private Approach[] extractApproaches(Scanner scanner) {
		approaches.clear();

		for (int i = 0; i < 2; i++) {
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
