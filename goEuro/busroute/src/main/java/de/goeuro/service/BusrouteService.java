package de.goeuro.service;

import org.springframework.stereotype.Service;

/**
 * A service for handling bus {@link Route routes} and its associated {@link Station Stations}.
 * 
 * @author du-it
 *
 */
@Service
public interface BusrouteService {
	public Boolean isDirectRouteWithStationsExistent(final Integer dep_sid, final Integer arr_sid);
	public Boolean isDirectRouteWithStationsExistent2(final Integer dep_sid, final Integer arr_sid);
}
