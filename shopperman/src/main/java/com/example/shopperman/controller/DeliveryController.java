package com.example.shopperman.controller;

import java.util.ArrayList;
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
import com.example.shopperman.entity.SortRequest;
import com.example.shopperman.service.MemberService;
import com.example.shopperman.service.PostService;
import com.example.shopperman.service.SecurityService;
import com.example.shopperman.util.TspBackTrackingAlgorithm;
import com.example.shopperman.util.TspGreedyAlgorithm;

@RestController
@RequestMapping("/delivery")
@Transactional
public class DeliveryController {

	@Autowired
	private PostService postService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private SecurityService securityService;

	// 최단거리 계산
	@PostMapping("/sort")
	public List<Location> sort(@RequestBody SortRequest sortRequest) {
	// 파라미터: myLocation, postIdList
		
//		 List<Location> locationList = new ArrayList<>();
//		  
//		 RequesterLocation a = new RequesterLocation();
//		 a.setAddr("a 주소");
//		 a.setMapX("2.0");
//		 a.setMapY("1.0");
//		 a.setId(1);
//		  
//		 RequesterLocation b = new RequesterLocation();
//		 b.setAddr("b 주소");
//		 b.setMapX("4.0");
//		 b.setMapY("2.0");
//		 b.setId(2);
//		  
//		 RequesterLocation c = new RequesterLocation();
//		 c.setAddr("c 주소");
//		 c.setMapX("-1.0");
//		 c.setMapY("2.0");
//		 c.setId(3);
//		  
//		 RequesterLocation d = new RequesterLocation();
//		 d.setAddr("d 주소");
//		 d.setMapX("0.0");
//		 d.setMapY("5.0");
//		 d.setId(4);
//		 
//		 RequesterLocation e = new RequesterLocation();
//		 e.setAddr("e 주소");
//		 e.setMapX("-3.0");
//		 e.setMapY("6.0");
//		 e.setId(5);
//		 
//		 RequesterLocation f = new RequesterLocation();
//		 f.setAddr("f 주소");
//		 f.setMapX("1.0");
//		 f.setMapY("4.0");
//		 f.setId(6);
//		 
//		 RequesterLocation g = new RequesterLocation();
//		 g.setAddr("g 주소");
//		 g.setMapX("3.0");
//		 g.setMapY("4.0");
//		 g.setId(7);
//		  
//		 MarketLocation sa = new MarketLocation();
//		 sa.setAddr("Sa 주소");
//		 sa.setMapX("1.0");
//		 sa.setMapY("3.0");
//		 ArrayList<String> saList = new ArrayList<>();
//		 saList.add("AAA");
//		 sa.setId(1);
//		  
//		 MarketLocation sb = new MarketLocation();
//		 sb.setAddr("Sb 주소");
//		 sb.setMapX("3.0");
//		 sb.setMapY("3.0");
//		 ArrayList<String> sbList = new ArrayList<>();
//		 sbList.add("BBB");
//		 sb.setId(2);
//		  
//		 MarketLocation sc = new MarketLocation();
//		 sc.setAddr("Sc 주소");
//		 sc.setMapX("2.0");
//		 sc.setMapY("5.0");
//		 ArrayList<String> scList = new ArrayList<>();
//		 scList.add("CCC");
//		 sc.setId(3);
//		  
//		 MarketLocation sd = new MarketLocation();
//		 sd.setAddr("Sd 주소");
//		 sd.setMapX("0.0");
//		 sd.setMapY("-1.0");
//		 ArrayList<String> sdList = new ArrayList<>();
//		 sdList.add("DDD");
//		 sd.setId(4);
//		 
//		 MarketLocation se = new MarketLocation();
//		 se.setAddr("Se 주소");
//		 se.setMapX("5.0");
//		 se.setMapY("6.0");
//		 ArrayList<String> seList = new ArrayList<>();
//		 seList.add("EEE");
//		 se.setId(5);
//		 
//		 MarketLocation sf = new MarketLocation();
//		 sf.setAddr("Sf 주소");
//		 sf.setMapX("-2.0");
//		 sf.setMapY("-3.0");
//		 ArrayList<String> sfList = new ArrayList<>();
//		 sfList.add("FFF");
//		 sf.setId(6);
//		 
//		 MarketLocation sg = new MarketLocation();
//		 sg.setAddr("Sg 주소");
//		 sg.setMapX("3.0");
//		 sg.setMapY("6.0");
//		 ArrayList<String> sgList = new ArrayList<>();
//		 sgList.add("GGG");
//		 sg.setId(7);
//		  
//		 locationList.add(a);
//		 locationList.add(b);
//		 locationList.add(c);
//		 locationList.add(d);
//		 locationList.add(e);
//		 locationList.add(f);
//		 
//		 locationList.add(sa);
//		 locationList.add(sb);
//		 locationList.add(sc);
//		 locationList.add(sd);
//		 locationList.add(se);
//		 locationList.add(sf);
//		 
//		 locationList.add(g);
//		 locationList.add(sg);
		 
		 

		//////////////////////////////////////////////////////////////////////
		 
		
		Location myLocation = sortRequest.getMyLocation();
		List<Integer> postIdList = sortRequest.getPostIdList();
		
		List<Post> postList = postService.getPostListByIdList(myLocation, postIdList);
		
		List<Location> locationList = new ArrayList<>();
		for(Post post : postList) {
			locationList.add(post.getRequesterLocation());
			locationList.add(post.getMarketLocation());
		}
		
		List<Location> orderedLocationList = null;
		// Location들이 12개 이하인 경우, BackTracking 알고리즘
		if(locationList.size() <= 12) {
			// BackTracking 알고리즘
	    	long startTime = System.currentTimeMillis();
	    	TspBackTrackingAlgorithm tsp = new TspBackTrackingAlgorithm(locationList);
	    	orderedLocationList = tsp.getTspOrderedLocationList(myLocation.getMapX(), myLocation.getMapY()); // 현재 위치 입력
	    	long endTime = System.currentTimeMillis();
	    	long duration = endTime - startTime;
	    	System.out.println("BackTracking 알고리즘 => " + duration + "밀리초");
	    	System.out.println();
		}
		// Location들이 12개 초과인 경우, Greedy 알고리즘
		else {
			// Greedy 알고리즘
			long startTime = System.currentTimeMillis();
	    	TspGreedyAlgorithm tsp = new TspGreedyAlgorithm(locationList);
	    	orderedLocationList = tsp.getTspOrderedLocationList(myLocation.getMapX(), myLocation.getMapY()); // 현재 위치 입력
	    	long endTime = System.currentTimeMillis();
	    	long duration = endTime - startTime;
	    	System.out.println("Greedy 알고리즘 => " + duration + "밀리초");
	    	System.out.println();
		}
    	
    	// 게시글 제목 추가하기
    	for(Location location : orderedLocationList) {
    		location.setTitle(postService.getTitle(location.getId()));
    	}
		
		return orderedLocationList;
	}

	// 배달 시작
	@PostMapping("/start")
	public String start(@RequestHeader(value = "Authorization") String token, @RequestBody List<Integer> postIdList) {
		// 파라미터: POST id 리스트

		String deliverymanNickname = (String)securityService.getSubject(token).get("nickname");
		
		for (Integer postId : postIdList) {
			if (postService.getState(postId) == 0) {
				// POST 테이블에 배달원 이름 적기
				boolean setDeliverymanNickname = postService.setDeliverymanNickname(postId, deliverymanNickname);

				// 게시글 State 바꾸기 (대기중 -> 배달중)
				boolean setState = postService.setState(postId, 1);

				// 하나라도 문제 생기면 FAILURE
				if (!setDeliverymanNickname || !setState) {
					return "{\"result\" : \"FAILURE\"}";
				}
			} else {
				return "{\"result\" : \"FAILURE\"}";
			}
		}

		return "{\"result\" : \"COMPLETE\"}";
	}

	// 배달 완료 신청
	@GetMapping("/complete")
	public String complete(@RequestHeader(value = "Authorization") String token, Integer postId) {

		String myNickname = (String)securityService.getSubject(token).get("nickname");

		// 현재 로그인된 사용자가 게시글의 배달원인 경우
		if (postService.getDeliverymanNickname(postId).equals(myNickname)) {
			if (postService.getState(postId) == 1) {
				// 게시글 State 바꾸기 (배달중 -> 배달 완료)
				if (postService.setState(postId, 2)) {
					// 배달원 포인트 증가
					String deliverymanNickname = postService.getDeliverymanNickname(postId);
					Integer deliveryTip = postService.getDeliveryTip(postId);
					if (deliverymanNickname == null || deliveryTip == null || !myNickname.equals(deliverymanNickname)) {
						return "{\"result\" : \"SERVICE_FAILURE\"}";
					}
					memberService.addPoint(deliverymanNickname, deliveryTip);
					return "{\"result\" : \"COMPLETE\"}";
				} else {
					return "{\"result\" : \"SERVICE_FAILURE\"}";
				}
			} else {
				return "{\"result\" : \"STATE_FAILURE\"}";
			}
		} else {
			return "{\"result\" : \"DELIVERYMAN_MISMATCHED_FAILURE\"}";
		}
	}
}
