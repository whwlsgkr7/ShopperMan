package com.example.shopperman.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopperman.dao.LocationDao;
import com.example.shopperman.entity.Location;
import com.example.shopperman.entity.MarketLocation;
import com.example.shopperman.entity.RequesterLocation;

@Repository("locationDao")
@Transactional
public class MybatisLocationDao implements LocationDao {

	private LocationDao mapper;
	
	@Autowired
    public MybatisLocationDao(SqlSession sqlSession) {
        mapper = sqlSession.getMapper(LocationDao.class);
    }
	
	// ---------------------------------------------------------------------------------
	
	@Override
	public List<RequesterLocation> getRequesterLocationList() {
		return mapper.getRequesterLocationList();
	}
	
	@Override
	public List<MarketLocation> getMarketLocationList() {
		return mapper.getMarketLocationList();
	}
	
	@Override
	public RequesterLocation getRequesterLocationById(Integer id) {
		return mapper.getRequesterLocationById(id);
	}
	
	@Override
	public MarketLocation getMarketLocationById(Integer id) {
		return mapper.getMarketLocationById(id);
	}

	@Override
	public List<RequesterLocation> getRequesterLocationListByRequesterName(String requesterName) {
		return mapper.getRequesterLocationListByRequesterName(requesterName);
	}
	
	@Override
	public List<MarketLocation> getMarketLocationListByRequesterName(String requesterName) {
		return mapper.getMarketLocationListByRequesterName(requesterName);
	}

	@Override
	public boolean setLocation(Location location) {
		return mapper.setLocation(location);
	}

	@Override
	public boolean deleteLocations(Integer id) {
		return mapper.deleteLocations(id);
	}
	
	@Override
	public List<Integer> getIdList() {
		return mapper.getIdList();
	}
	
	@Override
	public List<Integer> getIdListByRequesterName(String requesterName) {
		return mapper.getIdListByRequesterName(requesterName);
	}
}
