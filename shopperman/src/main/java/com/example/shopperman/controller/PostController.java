package com.example.shopperman.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopperman.entity.Location;
import com.example.shopperman.entity.Post;
import com.example.shopperman.service.LocationService;
import com.example.shopperman.service.MemberService;
import com.example.shopperman.service.PostService;
import com.example.shopperman.service.SecurityService;

@RestController
@RequestMapping("/post")
@Transactional
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private LocationService locationService;
	
	
	// 배달팁 계산
	@PostMapping("/deliveryTip/calculate")
	public Integer calculateDeliveryTip(@RequestBody Post locationSet) {
	// 파라미터: requesterLocation(mapX, mapY), marketLocation(mapX, mapY)

		// 배달팁 계산
		Integer distance = locationService.calculateDistance(locationSet.getRequesterLocation(), locationSet.getMarketLocation());
		Integer deliveryTip = postService.calculateDeliveryTip(distance);

		return deliveryTip;
	}
	
	// 게시글 등록
	@PostMapping("/create")
	public String createPost(@RequestHeader(value = "Authorization") String token, @RequestBody Post post) {
	// 파라미터: title, item, price, deliveryTip, content, requesterLocation(roadName, addr, mapX, mapY), marketLocation(roadName, marketName, mapX, mapY)
		
		if(token == null || token.equals("")) {
			return "{\"result\" : \"FAILURE\"}";
		}
		
		String currentUserId = (String)securityService.getSubject(token).get("id");
		String currentUserNickname = (String)securityService.getSubject(token).get("nickname");
		if(currentUserId == null || currentUserNickname == null) {
			return "{\"result\" : \"FAILURE\"}";
		}
		
		// 배달 요청자 포인트 차감
		String subtractPoint = memberService.subtractPoint(currentUserId, post.getDeliveryTip());
		if(subtractPoint.equals("Lack of points problem")) {
			return "{\"result\" : \"LACK_OF_POINTS\"}";
		}
		else if(subtractPoint.equals("Dao problem")) {
			return "{\"result\" : \"DAO_FAILURE\"}";
		}
		
		post.setPublisherNickname(currentUserNickname);
		post.getRequesterLocation().setRequesterName(currentUserNickname);
		post.getMarketLocation().setRequesterName(currentUserNickname);
		
		// 게시글 등록
		postService.createPost(post);
		
		return "{\"result\" : \"COMPLETE\"}";
	}
	
	// 게시글 리스트 조회 (배달하려는 사람 주소 넘겨주는 경우)
	@PostMapping("/get/list")
	public List<Post> getPostList(@RequestHeader(value = "Authorization") String token, @RequestBody Location location) {
	// 파라미터: mapX, mapY
		
		String currentUserNickname = "";
		if(token != null && !token.equals("")) {
			currentUserNickname = (String)securityService.getSubject(token).get("nickname");
		}
		
		return postService.getPostList(currentUserNickname, location);
	}
	
	// 게시글 id로, 해당 게시물 하나 조회
	@PostMapping("/get")
	public Post getPost(@RequestBody Location location, Integer id){
	// 파라미터: mapX, mapY
		
		return postService.getPost(location, id);
	}
	
	// 게시글 삭제
	@GetMapping("/delete")
	public String deletePost(Integer id) {
		if(postService.deletePost(id)) {
			return "{\"result\" : \"COMPLETE\"}";
		} else {
			return "{\"result\" : \"FAILURE\"}";
		}
	}
	
	// 게시자 이름으로 게시글 리스트 조회
	@GetMapping("/get/list/publish")
	public List<Post> getPostListByPublisherNickname(@RequestHeader(value = "Authorization") String token){
		
		if(token == null || token.equals("")) {
			return null;
		}
		
		String currentUserNickname = (String)securityService.getSubject(token).get("nickname");
		if(currentUserNickname == null) {
			return null;
		}
		
		return postService.getPostListByPublisherNickname(currentUserNickname);
	}
	
	// 배달원 이름으로 게시글 리스트 조회
	@GetMapping("/get/list/delivery")
	public List<Post> getPostListByDeliverymanNickname(@RequestHeader(value = "Authorization") String token){
		
		if(token == null || token.equals("")) {
			return null;
		}
		
		String currentUserNickname = (String)securityService.getSubject(token).get("nickname");
		if(currentUserNickname == null) {
			return null;
		}
		
		return postService.getPostListByDeliverymanNickname(currentUserNickname);
	}
}
