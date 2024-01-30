package com.example.shopperman.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MarketLocation extends Location {
	// private Integer id;         // 포스팅 번호
	// private String roadName;    // 도로명
	// private String addr;        // 주소
	// private String mapX;        // X 좌표
	// private String mapY;        // Y 좌표
	// private Integer type;       // Location 타입 (0: LOCATION / 1: REQUESTER / 2: MARKET)
	private String requesterName;  // 심부름을 요청한 사용자 이름
	private String marketName;     // 가게 이름
}
