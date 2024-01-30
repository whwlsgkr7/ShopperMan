package com.example.shopperman.dao;

import java.util.List;
import java.util.Map;

import com.example.shopperman.entity.Post;

public interface PostDao {

	// 게시글 등록
	void insertPost(Post post);

	// 게시글 리스트 가져오기
	List<Post> getPostList(String currentUserNickname);
	
	// 각각의 게시글 id에 대응하는 모든 게시글 가져오기
	List<Post> getPostListByIdList(List<Integer> idList);
	
	// 특정 게시글 가져오기
	Post getPost(Integer id);

	// 게시글 삭제
	boolean deletePost(Integer id);

	// 게시자 찾기
	String getPublisherNicknameByPostId(Integer id);

	// 게시글에 배달원 닉네임 적기
	boolean setDeliverymanNickname(Map<String, Object> idAndDeliverymanNickname);
	
	// 게시글의 배달원 닉네임 가져오기
	String getDeliverymanNickname(Integer id);
	
	// state 조회
	Integer getState(Integer id);
	
	// state 바꾸기
	boolean setState(Map<String, Integer> idAndState);

	// price 조회
	Integer getPrice(Integer id);

	// deliveryTip 조회
	Integer getDeliveryTip(Integer id);
	
	// 게시글 id로 게시글 제목 조회
	String getTitle(Integer id);

	// 게시자 이름으로 게시글 리스트 조회
	List<Post> getPostListByPublisherNickname(String nickname);

	// 배달원 이름으로 게시글 리스트 조회
	List<Post> getPostListByDeliverymanNickname(String nickname);
}
