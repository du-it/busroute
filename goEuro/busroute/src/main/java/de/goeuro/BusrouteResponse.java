package de.goeuro;

import com.fasterxml.jackson.annotation.JsonGetter;

public class BusrouteResponse {
	private Integer dep_sid;
    private Integer arr_sid;
    private Boolean direct_bus_route;
    
    @JsonGetter("dep_sid")
	public Integer getDep_sid() {
		return dep_sid;
	}
	public void setDep_sid(Integer dep_sid) {
		this.dep_sid = dep_sid;
	}
	
	@JsonGetter("arr_sid")
	public Integer getArr_sid() {
		return arr_sid;
	}
	public void setArr_sid(Integer arr_sid) {
		this.arr_sid = arr_sid;
	}
	
	@JsonGetter("direct_bus_route")
	public Boolean getDirect_bus_route() {
		return direct_bus_route;
	}
	public void setDirect_bus_route(Boolean direct_bus_route) {
		this.direct_bus_route = direct_bus_route;
	}
    
}
