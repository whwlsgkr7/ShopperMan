package com.example.shopperman.service;

import java.util.List;

import com.example.shopperman.entity.Location;
import com.example.shopperman.entity.Post;

public interface PostService {
	
	// 게시글 등록 (요청자 주소, 가게 주소도 등록)
	void createPost(Post post);
	
	// 현재 내 위치 근처 게시글 리스트 조회
	List<Post> getPostList(String currentUserNickname, Location location);
	
	// 각각의 게시글 id에 대응하는 모든 게시글 조회
	List<Post> getPostListByIdList(Location location, List<Integer> idList);
	
	// 특정 게시글 조회
	Post getPost(Location location, Integer id);
	
	// 특정 게시글 삭제
	boolean deletePost(Integer id);

	// 게시글 게시자 조회
	String getPublisherNicknameByPostId(Integer id);
	
	// 거리에 따른 배달팁 계산
	Integer calculateDeliveryTip(Integer distance);
	
	// 게시글에 배달원 닉네임 적기
	boolean setDeliverymanNickname(Integer id, String deliverymanNickname);
	
	// 게시글의 배달원 닉네임 조회
	String getDeliverymanNickname(Integer id);
	
	// 게시글 state 조회
	Integer getState(Integer id);
	
	// 게시글 state 바꾸기
	boolean setState(Integer id, Integer state);
	
	// 특정 게시글의 price 조회
	Integer getPrice(Integer id);
	
	// 특정 게시글의 deliveryTip 조회
	Integer getDeliveryTip(Integer id);
	
	// 게시글 id로 게시글 제목 조회
	String getTitle(Integer id);
	
	// 게시자 이름으로 게시글 리스트 조회
	List<Post> getPostListByPublisherNickname(String nickname);
	
	// 배달원 이름으로 게시글 리스트 조회
	List<Post> getPostListByDeliverymanNickname(String nickname);
}
