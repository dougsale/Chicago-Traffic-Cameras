package net.dougsale.chicagotrafficcameras.etl;

import java.io.Reader;
import java.util.Scanner;

import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Direction;
import net.dougsale.chicagotrafficcameras.domain.builders.RedLightCameraBuilder;

public class RedLightCameraCsvExtractor {
	
	public void extract(Reader source, Cameras cameras) {

		Scanner scanner = new Scanner(source);

		// discard column titles
		if (scanner.hasNextLine())
			scanner.nextLine();

		// make scanner.next() returns values by column (CSV)
		scanner.useDelimiter(",");

		RedLightCameraBuilder builder = new RedLightCameraBuilder();
		
		while (scanner.hasNext()) {

			extractIntersection(builder, scanner);
			extractApproaches(builder, scanner);

			// ignore "go live date"
			scanner.next();

			builder.withLocation(scanner.nextDouble(), scanner.nextDouble());

			// ignore "location"
			scanner.nextLine();

			cameras.add(builder.build());
			builder.reset();
		}
		
		scanner.close();
	}

	private void extractIntersection(RedLightCameraBuilder builder, Scanner scanner) {
		String intersection = scanner.next();
		
		// replace erroneous question marks with spaces
		intersection = intersection.replace('?', ' ');
		for (String street : intersection.split("-"))
			builder.withStreet(street);
	}

	private void extractApproaches(RedLightCameraBuilder builder, Scanner scanner) {

		for (int i = 0; i < 3; i++) {
			String approach = scanner.next();
			switch (approach) {
			case "NB":
				builder.withApproach(Direction.NORTHBOUND);
				break;
			case "NEB":
				builder.withApproach(Direction.NORTHEASTBOUND);
				break;
			case "NWB":
				builder.withApproach(Direction.NORTHWESTBOUND);
				break;
			case "SB":
				builder.withApproach(Direction.SOUTHBOUND);
				break;
			case "SEB":
				builder.withApproach(Direction.SOUTHEASTBOUND);
				break;
			case "SWB":
				builder.withApproach(Direction.SOUTHWESTBOUND);
				break;
			case "EB":
				builder.withApproach(Direction.EASTBOUND);
				break;
			case "WB":
				builder.withApproach(Direction.WESTBOUND);
				break;
			case "":
				// okay
				break;
			default:
				System.err.println("Unhandled approach: " + approach);
				break;
			}
		}
	}

}
