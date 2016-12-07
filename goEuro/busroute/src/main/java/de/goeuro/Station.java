/**
 * 
 */
package de.goeuro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A <code>Station</code> represents a bus station and is defined by its unique ID.
 * 
 * @author du-it
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {
	private int uid;
	
	public Station(final int uid) {
		this.uid = uid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
}
