/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.domain;

/**
 * @author dsale
 *
 */
public enum Direction {
	NORTHBOUND, NORTHEASTBOUND, NORTHWESTBOUND,
	SOUTHBOUND, SOUTHEASTBOUND, SOUTHWESTBOUND,
	EASTBOUND, WESTBOUND
}
//TODO possibly... create EnumSet.of(Approach) for Location[start, end]
// this wouldn't be an Approach, but a Heading
// which means that this shouldn't be Approach but Direction (which makes Directions a confusing class name)
// end game for this idea is filtering cameras by comparing *Camera Approach(s) to Directions.Step Direction(s)