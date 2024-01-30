package com.example.shopperman.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopperman.entity.Coordinate;
import com.example.shopperman.entity.Location;
import com.example.shopperman.entity.LocationContainer;
import com.example.shopperman.service.LocationService;

@RestController
@RequestMapping("/location")
public class LocationController {
	
	@Autowired
	private LocationService locationService;
	
	
	// Location 리스트 가져오기
	@GetMapping("/get/list")
	public List<LocationContainer> getLocationsList() {
		return locationService.getLocationsList();
	}
	
	// Location id(숫자)를 이용해서, Location 가져오기
	@GetMapping("/getById")
	public LocationContainer getLocationsById(Integer id) {
		return locationService.getLocationsById(id);
	}
	
	// 심부름 요청자 이름으로 Location 리스트 찾기
	@GetMapping("/getByName/list")
	public List<LocationContainer> getLocationsByRequesterName(String requesterName){
		return locationService.getLocationsListByRequesterName(requesterName);
	}
	
	// 새로운 Location 넣기
	@PostMapping("/set/common")
	public String setLocation(@RequestBody Location location) {
	// 파라미터: roadName, mapX, mapY
		
		if(locationService.setLocation(location)) {
			return "{\"result\" : \"SUCCESS\"}";
		} else {
			return "{\"result\" : \"FAILURE\"}";
		}
	}
	
	// 도로명 주소 -> 좌표 변환하기
	@GetMapping("/geo")
	public Coordinate geo(String roadName) {
		
		Coordinate coordinate = locationService.getCoordinate(roadName);
		
		return coordinate;
	}
	
	// 좌표 -> 도로명 주소 변환하기
	@GetMapping(value = "/reversegeo", produces = "text/plain;charset=UTF-8")
	public String reversegeo(String mapX, String mapY) {
		
		String roadName = locationService.getRoadName(mapX, mapY);
		
		return "{\"juso\" : \"" + roadName + "\"}";
	}
}
