package edu.brown.cs.student.main.server;

/**
 * This class stores BroadbandData
 *
 * @param requestedLocation type string of queried location
 * @param broadband type string of broadband
 * @param timeOfRequest type string of time of query
 */
public record BroadbandData(String requestedLocation, String broadband, String timeOfRequest) {}
