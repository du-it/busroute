package de.goeuro.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import de.goeuro.Route;
import de.goeuro.Station;

/**
 * 
 * 
 * @author du-it
 *
 */
@Component
@PropertySource("classpath:application.properties")
public class BusrouteServiceImpl implements BusrouteService {
	
	private static Map<Integer, Route> busroutesMap;
//	private static long busrouteDataFileReadingDate;
	private static ResourceLoader resourceLoader;
	private static Boolean directRouteExists = Boolean.FALSE;
	@Value("${busrouteDataFile}")
	private String busrouteDataFile;
	
	private Logger logger = Logger.getLogger(BusrouteServiceImpl.class);

    @Autowired
	public void setResourceLoader(ResourceLoader resourceLoaderIn) {
		resourceLoader = resourceLoaderIn;
	}
    
    /**
     * Performs additional initializations after the service is loaded/constructed.
     * Here, the bus route data file is read in.
     */
	@PostConstruct
	public void init() {
		System.out.println("Using bus route data file: " + busrouteDataFile + "...");
		final Resource resource = resourceLoader.getResource("classpath:" + busrouteDataFile);
//		final Resource resource = resourceLoader.getResource("classpath:static/busroute/busrouteDataFile.txt");
//		String brdf = new SpringApplicationBuilder(BusrouteConfiguration.class).properties("busrouteDataFile").;
//    	PropertySource<?> pSource = new SimpleCommandLinePropertySource(brdf);

		List<String> list = new ArrayList<>();

		// First check whether the new bus route data file is newer than the date of the last import...
		try{
			final InputStream is = resource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			list = br.lines().collect(Collectors.toList());
		}catch(IOException e){
			//TODO: 
			e.printStackTrace();
		}

		list.forEach(System.out::println);

		// Fill map with bus routes...
		fillBusrouteMap(list);

//		busrouteDataFileReadingDate = System.currentTimeMillis();

		System.out.println(resource.getFilename() + " read.");
	}// end init
	
	/**
	 * Fills an internal {@link TreeMap} with {@link Station Stations} per {@link Route} whereby 
	 * the {@Route Route's} UID functions a the map's <code>key</code> and the {@Route Route's} list 
	 * of {@link Station Stations} is the map's <code>value</code>.
	 * 
	 * @param busrouteList	The list of 
	 */
	private void fillBusrouteMap(List<String> busrouteList) {
		busroutesMap = new TreeMap<Integer, Route>();

		busrouteList.forEach((zeile) -> {
			if( zeile.contains(" ") ) {
				System.out.println("Zeile ist eine Route: " + zeile);
				List<String> stations = Arrays.asList(zeile.split(" "));

				// TODO: Java 8 style possible???
				Integer routeId = new Integer(stations.get(0));
				ListIterator<String> listIterator = stations.listIterator();
				LinkedHashSet<Station> routeStations = new LinkedHashSet<Station>(stations.size()-1);
				if( listIterator.hasNext() ) {
					listIterator.next();
				}
				while( listIterator.hasNext()) {
					String station = listIterator.next();
					routeStations.add(new Station(new Integer(station)));
				}

				final Route route = new Route(routeId, routeStations);
				busroutesMap.put(routeId, route);
				
			} else {
				List<String> routes = Arrays.asList(zeile.split(" "));
				System.out.println("Zeile gibt die Anzahl der Routen im busrouteDataFile mit " + routes.get(0) + " Routen an.");
			}
		});
		System.out.println("busrouteMap filled...\n");
	}//end fillBusrouteMap
	
	/**
	 * Checks whether {@link Route Routes} exist containing the provided stations. 
	 * This method loops over all {@link Station Stations} in all {@link Route Routes} 
	 * stored in the internal map storing these {@link Route Routes}.
	 * 
	 * @param dep_sid	The UID of the departure station ID.
	 * @param arr_sid	The UID of the arrival station ID.
	 * 
	 * @return		<code>true</code> if a {@link Route} exists containing both station IDs,
	 * 				<code>false</code> otherwise. 
	 * 
	 */
	@Override
	public Boolean isDirectRouteWithStationsExistent(Integer dep_sid, Integer arr_sid) {
		System.out.println("dep_sid = " + dep_sid + ", arr_sid = " + arr_sid);

		busroutesMap.forEach((routeId,route)->{
		System.out.println("Route : " + route.getUid());
		if(route.getStations().stream().filter(
				station -> station.getUid() == dep_sid || station.getUid() == arr_sid)
			.collect(Collectors.toSet()).size() > 1) {
				System.out.println("Direkte Busroute gefunden: " + route.getUid() + "."); 
				BusrouteServiceImpl.directRouteExists = Boolean.TRUE;
				} else {
					System.out.println("Keine direkte Verbindung der Stationen " 
							+ dep_sid + " und " + arr_sid + " in Route " + route.getUid() +  " gefunden."); 
					BusrouteServiceImpl.directRouteExists = Boolean.FALSE;
				};
		});//end forEach((routeId,route)

		return BusrouteServiceImpl.directRouteExists;
	}

	/**
	 * Checks whether {@link Route Routes} exist containing the provided stations.
	 * As soon as a direct route is found which connects both {@link Station Stations} 
	 * this method returns.
	 * 
	 * @param dep_sid	The UID of the departure station ID.
	 * @param arr_sid	The UID of the arrival station ID.
	 * 
	 * @return		<code>true</code> if a {@link Route} exists containing both station IDs,
	 * 				<code>false</code> otherwise. 
	 * 
	 */
	@Override
	public Boolean isDirectRouteWithStationsExistent2(Integer dep_sid, Integer arr_sid) {
		System.out.println("dep_sid = " + dep_sid + ", arr_sid = " + arr_sid);

		
		for (Map.Entry<Integer, Route> entry : busroutesMap.entrySet()) {
System.out.println("Route ID = " + entry.getKey() + ": ");
			boolean dep_sid_found = false;
			boolean arr_sid_found = false;
		    for(Station station : entry.getValue().getStations()){
System.out.println(station.getUid());
		    	if(station.getUid() == dep_sid){
		    		dep_sid_found = true;
		    	} else if(station.getUid() == arr_sid){
		    		arr_sid_found = true;
		    	}
		    }//end for station
			if(dep_sid_found == true   && arr_sid_found == true) {
				return Boolean.TRUE;
			}
		}//end for entry

		return BusrouteServiceImpl.directRouteExists;
	}

}
