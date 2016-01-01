package net.dougsale.chicagotrafficcameras.etl;

import java.io.Reader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import net.dougsale.chicagotrafficcameras.domain.Approach;
import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.SpeedCamera;

public class SpeedCameraCsvExtractor {
	
	private HashSet<Approach> approaches = new HashSet<>(2);
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	public void extract(Reader source, Cameras cameras) {

		Scanner scanner = new Scanner(source);

		// discard column titles
		if (scanner.hasNextLine())
			scanner.nextLine();

		// make scanner.next() returns values by column (CSV)
		scanner.useDelimiter(",");

		while (scanner.hasNext()) {

			String address = extractAddress(scanner);
			Set<Approach> approaches = extractApproaches(scanner);

			// ignore "go live date"
			scanner.next();

			double latitude = scanner.nextDouble();
			double longitude = scanner.nextDouble();

			// ignore "location"
			scanner.nextLine();

			SpeedCamera camera =
					new SpeedCamera(address, new Location(latitude, longitude), approaches);
			cameras.add(camera);
		}

		scanner.close();
	}

	private String extractAddress(Scanner scanner) {
		String address = scanner.next();
		address = address.replace(" (Speed Camera)", "");
		return address;
	}

	private Set<Approach> extractApproaches(Scanner scanner) {
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

		return approaches;
	}

}
