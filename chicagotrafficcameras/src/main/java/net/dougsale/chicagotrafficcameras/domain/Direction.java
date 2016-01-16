/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dsale
 *
 */
public enum Direction {
	NORTHBOUND, NORTHEASTBOUND, NORTHWESTBOUND,
	SOUTHBOUND, SOUTHEASTBOUND, SOUTHWESTBOUND,
	EASTBOUND, WESTBOUND;
	
	private static final Map<String,Direction> lookup = new HashMap<>();
	static {
		lookup.put("NORTH", NORTHBOUND);
		lookup.put("SOUTH", SOUTHBOUND);
		lookup.put("EAST", EASTBOUND);
		lookup.put("WEST", WESTBOUND);
		lookup.put("NORTHEAST", NORTHEASTBOUND);
		lookup.put("NORTHWEST", NORTHWESTBOUND);
		lookup.put("SOUTHEAST", SOUTHEASTBOUND);
		lookup.put("SOUTHWEST", SOUTHWESTBOUND);

		lookup.put("NORTHBOUND", NORTHBOUND);
		lookup.put("SOUTHBOUND", SOUTHBOUND);
		lookup.put("EASTBOUND", EASTBOUND);
		lookup.put("WESTBOUND", WESTBOUND);
		lookup.put("NORTHEASTBOUND", NORTHEASTBOUND);
		lookup.put("NORTHWESTBOUND", NORTHWESTBOUND);
		lookup.put("SOUTHEASTBOUND", SOUTHEASTBOUND);
		lookup.put("SOUTHWESTBOUND", SOUTHWESTBOUND);

		lookup.put("NB", NORTHBOUND);
		lookup.put("SB", SOUTHBOUND);
		lookup.put("EB", EASTBOUND);
		lookup.put("WB", WESTBOUND);
		lookup.put("NEB", NORTHEASTBOUND);
		lookup.put("NWB", NORTHWESTBOUND);
		lookup.put("SEB", SOUTHEASTBOUND);
		lookup.put("SWB", SOUTHWESTBOUND);
	}
	
	/**
	 * Returns a Direction enum value for the given string.
	 * @param direction string representation
	 * @return Direction enum value
	 * @throws NullPointerException if direction is null
	 */
	public static Direction fromString(String direction) {
		notNull(direction, "invalid parameter: direction=null");
		
		Direction value = lookup.get(direction.trim().toUpperCase());
		
		if (value == null)
			throw new IllegalArgumentException(String.format("\"{}\" is not an accepted Direction representation", direction));
		
		return value;
	}
}