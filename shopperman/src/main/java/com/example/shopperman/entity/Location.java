package com.example.shopperman.entity;

import lombok.Data;

@Data
public class Location {
	private Integer id;       // 포스팅 번호
	private String title;     // 게시글 제목
	private String roadName;  // 도로명
	private String addr;      // 주소
	private String mapX;      // X 좌표
	private String mapY;      // Y 좌표
	private Integer type;     // Location 타입 (0: LOCATION / 1: REQUESTER / 2: MARKET)
}
