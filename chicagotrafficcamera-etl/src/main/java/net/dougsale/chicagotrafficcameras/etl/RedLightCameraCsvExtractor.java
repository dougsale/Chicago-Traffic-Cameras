package net.dougsale.chicagotrafficcameras.etl;

import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import net.dougsale.chicagotrafficcameras.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Approach;
import net.dougsale.chicagotrafficcameras.domain.Location;
import net.dougsale.chicagotrafficcameras.domain.RedLightCamera;

public class RedLightCameraCsvExtractor {
	
	private Set<Approach> approaches = new HashSet<>(3);
	
	public void extract(Reader source, Cameras cameras) {

		Scanner scanner = new Scanner(source);

		// discard column titles
		if (scanner.hasNextLine())
			scanner.nextLine();

		// make scanner.next() returns values by column (CSV)
		scanner.useDelimiter(",");

		while (scanner.hasNext()) {

			Set<String> intersection = extractIntersection(scanner);
			Set<Approach> approaches = extractApproaches(scanner);

			// ignore "go live date"
			scanner.next();

			double latitude = scanner.nextDouble();
			double longitude = scanner.nextDouble();

			// ignore "location"
			scanner.nextLine();

			RedLightCamera camera =
					new RedLightCamera(intersection, new Location(latitude, longitude), approaches);
			cameras.add(camera);
			
//			System.out.println(camera);
		}
		
		scanner.close();
	}

	private Set<String> extractIntersection(Scanner scanner) {
		String intersection = scanner.next();
		
		// replace erroneous question marks with spaces
		intersection = intersection.replace('?', ' ');
		return new HashSet<>(Arrays.asList(intersection.split("-")));
	}

	private Set<Approach> extractApproaches(Scanner scanner) {
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

		return approaches;
	}

}
