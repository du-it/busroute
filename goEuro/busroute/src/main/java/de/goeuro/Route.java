package de.goeuro;

import java.util.LinkedHashSet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A <code>Route</code> represents a bus route which has a unique ID and a number of stations.
 * 
 * @author du-it
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {
	private int uid;
	private LinkedHashSet<Station> stations;
	
	public Route(final int uid) {
		stations = new LinkedHashSet<Station>();
	}

	public Route(final int uid, LinkedHashSet<Station> stations) {
		this.uid = uid;
		this.stations = stations;
	}
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public LinkedHashSet<Station> getStations() {
		return stations;
	}
	public void setStations(LinkedHashSet<Station> stations) {
		this.stations = stations;
	}
	public void add(final Station station) {
		stations.add(station);
	}
}
