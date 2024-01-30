package com.example.shopperman.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopperman.dao.LocationDao;
import com.example.shopperman.dao.PostDao;
import com.example.shopperman.entity.Location;
import com.example.shopperman.entity.MarketLocation;
import com.example.shopperman.entity.Post;
import com.example.shopperman.entity.RequesterLocation;

@Service("postService")
@Transactional
public class PostServiceImp implements PostService {
	
	@Autowired
	private PostDao postDao;
	
	@Autowired
	private LocationDao locationDao;
	
	@Autowired
	private LocationService locationService;
	
	// ---------------------------------------------------------------------------------

	@Override
	public void createPost(Post post) {
		postDao.insertPost(post);
	}
	
	@Override
	public List<Post> getPostList(String currentUserNickname, Location location) {
		
		// 모든 게시물을 최근 순으로 나열
		List<Post> postListWithoutLocationInfo = postDao.getPostList(currentUserNickname);
		
		List<Post> postList = new ArrayList<>();
		for(Post post : postListWithoutLocationInfo) {
			
			// 게시물이 10개가 넘어가면, 10개까지만 반환
			if(postList.size() >= 10) {
				return postList;
			}
			
			RequesterLocation requesterLocation = locationDao.getRequesterLocationById(post.getId());
			MarketLocation marketLocation = locationDao.getMarketLocationById(post.getId());
			
			Integer deliveryToRequesterDistance = null;
			if(requesterLocation != null) {
				// 배달하는 사람과 배달받을 사람 사이의 거리(미터)
				deliveryToRequesterDistance = locationService.calculateDistance(location, requesterLocation);
			}
			Integer deliveryToMarketDistance = null;
			if(marketLocation != null) {
				// 배달하는 사람과 가게 사이의 거리(미터)
				deliveryToMarketDistance = locationService.calculateDistance(location, marketLocation);
			}
			
			// 요청자 위치 & 가게 위치 각각 5000m 거리 제한
			if(deliveryToRequesterDistance != null && deliveryToMarketDistance != null) {
				if(deliveryToRequesterDistance <= 5000 && deliveryToMarketDistance <= 5000) {
					post.setRequesterLocation(requesterLocation);
					post.setMarketLocation(marketLocation);
					post.setDeliveryToRequesterDistance(deliveryToRequesterDistance);
					post.setDeliveryToMarketDistance(deliveryToMarketDistance);
					postList.add(post);
				}
			}
		}
		
		return postList;
	}
	
	@Override
	public List<Post> getPostListByIdList(Location location, List<Integer> idList) {
		
		// 각각의 게시물 id에 대응하는 게시물 리스트 
		List<Post> postListWithoutLocationInfo = postDao.getPostListByIdList(idList);
		
		List<Post> postList = new ArrayList<>();
		for(Post post : postListWithoutLocationInfo) {
			
			RequesterLocation requesterLocation = locationDao.getRequesterLocationById(post.getId());
			MarketLocation marketLocation = locationDao.getMarketLocationById(post.getId());
			
			Integer deliveryToRequesterDistance = null;
			if(requesterLocation != null) {
				// 배달하는 사람과 배달받을 사람 사이의 거리(미터)
				deliveryToRequesterDistance = locationService.calculateDistance(location, requesterLocation);
			}
			Integer deliveryToMarketDistance = null;
			if(marketLocation != null) {
				// 배달하는 사람과 가게 사이의 거리(미터)
				deliveryToMarketDistance = locationService.calculateDistance(location, marketLocation);
			}
			
			post.setRequesterLocation(requesterLocation);
			post.setMarketLocation(marketLocation);
			post.setDeliveryToRequesterDistance(deliveryToRequesterDistance);
			post.setDeliveryToMarketDistance(deliveryToMarketDistance);
			postList.add(post);
		}
		
		return postList;
	}

	@Override
	public Post getPost(Location location, Integer id) {
		
		Post post = postDao.getPost(id);
		
		if(post == null) {
			return null;
		}
		
		RequesterLocation requesterLocation = locationDao.getRequesterLocationById(post.getId());
		MarketLocation marketLocation = locationDao.getMarketLocationById(post.getId());
		
		Integer deliveryToRequesterDistance = null;
		if(requesterLocation != null) {
			// 배달하는 사람과 배달받을 사람 사이의 거리(미터) 삽입
			deliveryToRequesterDistance = locationService.calculateDistance(location, requesterLocation);
		}
		Integer deliveryToMarketDistance = null;
		if(marketLocation != null) {
			// 배달하는 사람과 가게 사이의 거리(미터)
			deliveryToMarketDistance = locationService.calculateDistance(location, marketLocation);
		}
		
		post.setRequesterLocation(locationDao.getRequesterLocationById(id));
		post.setMarketLocation(locationDao.getMarketLocationById(id));
		post.setDeliveryToRequesterDistance(deliveryToRequesterDistance);
		post.setDeliveryToMarketDistance(deliveryToMarketDistance);
		
		return post;
	}

	@Override
	public boolean deletePost(Integer id) {
		boolean deleteLocations = locationDao.deleteLocations(id);
		boolean deletePost = postDao.deletePost(id);
		return deleteLocations && deletePost;
	}

	@Override
	public String getPublisherNicknameByPostId(Integer id) {
		return postDao.getPublisherNicknameByPostId(id);
	}

	@Override
	public Integer calculateDeliveryTip(Integer distance) {
    	
    	// 1000m 당 3000 포인트
    	double roundedDistance = Math.round((double)distance / 100) * 100;
    	Integer deliveryTip = (int)(roundedDistance / 1000 * 3000);
		
		return deliveryTip;
	}
	
	@Override
	public boolean setDeliverymanNickname(Integer id, String deliverymanNickname) {
		
		Map<String, Object> idAndDeliverymanNickname = new HashedMap<>();
		idAndDeliverymanNickname.put("id", id);
		idAndDeliverymanNickname.put("deliverymanNickname", deliverymanNickname);
		
		return postDao.setDeliverymanNickname(idAndDeliverymanNickname);
	}
	
	@Override
	public String getDeliverymanNickname(Integer id) {
		return postDao.getDeliverymanNickname(id);
	}
	
	@Override
	public Integer getState(Integer id) {
		return postDao.getState(id);
	}

	@Override
	public boolean setState(Integer id, Integer state) {
		
		Map<String, Integer> idAndState = new HashedMap<>();
		idAndState.put("id", id);
		idAndState.put("state", state);
		
		return postDao.setState(idAndState);
	}

	@Override
	public Integer getPrice(Integer id) {
		return postDao.getPrice(id);
	}

	@Override
	public Integer getDeliveryTip(Integer id) {
		return postDao.getDeliveryTip(id);
	}
	
	@Override
	public String getTitle(Integer id) {
		return postDao.getTitle(id);
	}

	@Override
	public List<Post> getPostListByPublisherNickname(String nickname) {
		
		List<Post> postListWithoutLocationInfo = postDao.getPostListByPublisherNickname(nickname);
		
		List<Post> postList = new ArrayList<>();
		for(Post post : postListWithoutLocationInfo) {
			
			RequesterLocation requesterLocation = locationDao.getRequesterLocationById(post.getId());
			MarketLocation marketLocation = locationDao.getMarketLocationById(post.getId());
			
			post.setRequesterLocation(requesterLocation);
			post.setMarketLocation(marketLocation);
			postList.add(post);
		}
		
		return postList;
	}

	@Override
	public List<Post> getPostListByDeliverymanNickname(String nickname) {
		
		List<Post> postListWithoutLocationInfo = postDao.getPostListByDeliverymanNickname(nickname);
		
		List<Post> postList = new ArrayList<>();
		for(Post post : postListWithoutLocationInfo) {
			
			RequesterLocation requesterLocation = locationDao.getRequesterLocationById(post.getId());
			MarketLocation marketLocation = locationDao.getMarketLocationById(post.getId());
			
			post.setRequesterLocation(requesterLocation);
			post.setMarketLocation(marketLocation);
			postList.add(post);
		}
		
		return postList;
	}
}
