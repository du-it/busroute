package de.goeuro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.goeuro.service.BusrouteService;

@RestController
@EnableAutoConfiguration
@ComponentScan(basePackages = { "de.goeuro","de.goeuro.service"})
//@RequestMapping("/api/direct")
public class BusrouteController {

	@Autowired
	private BusrouteService busrouteService;
	
    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }
    
    @Autowired
	public void setBusrouteService(BusrouteService busrouteService) {
    	this.busrouteService = busrouteService;
	}
	
    @RequestMapping(value="/api/direct", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    BusrouteResponse doesRouteExist(@RequestParam(value="dep_sid") final Integer dep_sid, 
    			   @RequestParam(value="arr_sid") final Integer arr_sid
//    			   , @RequestParam(value="pathToBusrouteDataFile") String busrouteDataFile
    			   ) {
    	System.out.println("Does a busroute exist which contains the station IDs " + dep_sid + " and " + arr_sid + "?");
    	Boolean routeExists = busrouteService.isDirectRouteWithStationsExistent(dep_sid, arr_sid);
//    	Boolean routeExists = busrouteService.isDirectRouteWithStationsExistent2(dep_sid, arr_sid);
    	BusrouteResponse response = new BusrouteResponse();
    	response.setArr_sid(arr_sid);
    	response.setDep_sid(dep_sid);
    	response.setDirect_bus_route(routeExists);
    	return response;
    }
}
