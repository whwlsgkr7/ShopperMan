package com.example.shopperman.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.shopperman.dao.LocationDao;
import com.example.shopperman.entity.Coordinate;
import com.example.shopperman.entity.Location;
import com.example.shopperman.entity.LocationContainer;
import com.example.shopperman.entity.MarketLocation;
import com.example.shopperman.entity.RequesterLocation;

@Service("locationService")
@PropertySource("classpath:local.properties")
public class LocationServiceImp implements LocationService {

	@Autowired
	private LocationDao locationDao;

	@Value("${KAKAO_REST_API_KEY}")
	private String kakaoRestApiKey;

	// ---------------------------------------------------------------------------------

	@Override
	public List<LocationContainer> getLocationsList() {

		List<Integer> idList = locationDao.getIdList();
		List<RequesterLocation> requesterLocationList = locationDao.getRequesterLocationList();
		List<MarketLocation> marketLocationList = locationDao.getMarketLocationList();

		List<LocationContainer> locationContainerList = new ArrayList<>();
		for (Integer id : idList) {
			LocationContainer locationContainer = new LocationContainer();
			for (RequesterLocation requesterLocation : requesterLocationList) {
				if (requesterLocation.getId() == id) {
					locationContainer.setRequesterLocation(requesterLocation);
				}
			}
			for (MarketLocation marketLocation : marketLocationList) {
				if (marketLocation.getId() == id) {
					locationContainer.setMarketLocation(marketLocation);
				}
			}
			locationContainerList.add(locationContainer);
		}

		return locationContainerList;
	}

	@Override
	public LocationContainer getLocationsById(Integer id) {

		LocationContainer locationContainer = new LocationContainer();

		locationContainer.setRequesterLocation(locationDao.getRequesterLocationById(id));
		locationContainer.setMarketLocation(locationDao.getMarketLocationById(id));

		return locationContainer;
	}

	@Override
	public List<LocationContainer> getLocationsListByRequesterName(String requesterName) {

		List<Integer> idList = locationDao.getIdListByRequesterName(requesterName);
		List<RequesterLocation> requesterLocationList = locationDao
				.getRequesterLocationListByRequesterName(requesterName);
		List<MarketLocation> marketLocationList = locationDao.getMarketLocationListByRequesterName(requesterName);

		List<LocationContainer> locationContainerList = new ArrayList<>();
		for (Integer id : idList) {
			LocationContainer locationContainer = new LocationContainer();
			for (RequesterLocation requesterLocation : requesterLocationList) {
				if (requesterLocation.getId() == id) {
					locationContainer.setRequesterLocation(requesterLocation);
				}
			}
			for (MarketLocation marketLocation : marketLocationList) {
				if (marketLocation.getId() == id) {
					locationContainer.setMarketLocation(marketLocation);
				}
			}
			locationContainerList.add(locationContainer);
		}

		return locationContainerList;
	}

	@Override
	public boolean setLocation(Location location) {
		return locationDao.setLocation(location);
	}

	@Override
	public Integer calculateDistance(Location locationA, Location locationB) {

		BigDecimal locationADecimalX = new BigDecimal(locationA.getMapX());
		BigDecimal locationBDecimalX = new BigDecimal(locationB.getMapX());

		BigDecimal locationADecimalY = new BigDecimal(locationA.getMapY());
		BigDecimal locationBDecimalY = new BigDecimal(locationB.getMapY());

		BigDecimal x = locationADecimalX.subtract(locationBDecimalX).multiply(new BigDecimal(88));
		BigDecimal y = locationADecimalY.subtract(locationBDecimalY).multiply(new BigDecimal(111));
		BigDecimal x2 = x.pow(2);
		BigDecimal y2 = y.pow(2);
		MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
		BigDecimal distanceDecimal = x2.add(y2).sqrt(mc);

		Integer distance = (int) (distanceDecimal.doubleValue() * 1000);

		return distance;
	}

	@Override
	public Coordinate getCoordinate(String roadName) {

		String uriString = "https://dapi.kakao.com/v2/local/search/address?query=" + roadName;

		// HttpHeaders 객체 생성 및 설정
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

		// HttpEntity 객체 생성
		HttpEntity<String> request = new HttpEntity<>(headers);

		// RestTemplate 생성
		RestTemplate restTemplate = new RestTemplate();
		// api 호출
		ResponseEntity<String> responseEntity = restTemplate.exchange(uriString, HttpMethod.GET, request, String.class);
		// response body
		JSONObject body = new JSONObject(responseEntity.getBody());
		// 주소 읽어오기
		JSONArray documents = body.getJSONArray("documents");
		
		if (documents == null) {
			return null;
		}
		if (documents.length() == 0) {
			return null;
		}
		if (documents.getJSONObject(0).isEmpty()) {
			return null;
		}
		if (documents.getJSONObject(0).get("x") == null || documents.getJSONObject(0).get("y") == null) {
			return null;
		}
		String mapX = (String) documents.getJSONObject(0).get("x");
		String mapY = (String) documents.getJSONObject(0).get("y");

		Coordinate coordinate = new Coordinate();
		coordinate.setMapX(mapX);
		coordinate.setMapY(mapY);
		
		return coordinate;
	}

	@Override
	public String getRoadName(String mapX, String mapY) {

		String uriString = "https://dapi.kakao.com/v2/local/geo/coord2address?x=" + mapX + "&y=" + mapY;

		// HttpHeaders 객체 생성 및 설정
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

		// HttpEntity 객체 생성
		HttpEntity<String> request = new HttpEntity<>(headers);

		// RestTemplate 생성
		RestTemplate restTemplate = new RestTemplate();
		// api 호출
		ResponseEntity<String> responseEntity = restTemplate.exchange(uriString, HttpMethod.GET, request, String.class);
		// response body
		JSONObject body = new JSONObject(responseEntity.getBody());
		// 주소 읽어오기
		JSONArray documents = body.getJSONArray("documents");
		
		if (documents == null) {
			return "No_Documents";
		}
		if (documents.length() == 0) {
			return "Empty_Documents";
		}
		if (documents.getJSONObject(0).isEmpty()) {
			return "Empty_Road_Address";
		}
		if (documents.getJSONObject(0).getJSONObject("road_address").isEmpty()) {
			return "Empty_Road_Address_Name";
		}
		String roadName = (String) documents.getJSONObject(0).getJSONObject("road_address").get("address_name");

		return roadName;
	}
}
