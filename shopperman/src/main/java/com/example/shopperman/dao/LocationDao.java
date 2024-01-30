package com.example.shopperman.dao;

import java.util.List;

import com.example.shopperman.entity.Location;
import com.example.shopperman.entity.MarketLocation;
import com.example.shopperman.entity.RequesterLocation;

public interface LocationDao {

	// get Requester Location List
	List<RequesterLocation> getRequesterLocationList();
	
	// get Market Location List
	List<MarketLocation> getMarketLocationList();
	
	// get RequesterLocation by ID
	RequesterLocation getRequesterLocationById(Integer id);
	
	// get MarketLocation by ID
	MarketLocation getMarketLocationById(Integer id);

	// get RequesterLocations by Requester Name
	List<RequesterLocation> getRequesterLocationListByRequesterName(String requesterName);
	
	// get MarketLocations by Requester Name
	List<MarketLocation> getMarketLocationListByRequesterName(String requesterName);

	// set Location
	boolean setLocation(Location location);
	
	// delete Locations by ID
	boolean deleteLocations(Integer id);
	
	// 게시물 ID 리스트 가져오기
	List<Integer> getIdList();
	
	// 게시물 작성자 닉네임에 따른, 게시물 ID 리스트 가져오기
	List<Integer> getIdListByRequesterName(String requesterName);
}
