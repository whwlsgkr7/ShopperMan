package com.example.shopperman.entity;

import lombok.Data;

@Data
public class Post {
	private Integer id;                          // 시퀀스 번호
	private String publisherNickname;            // 게시글 게시자 닉네임
	private String title;                        // 제목
	private String item;                         // 심부름 받을 물건
	private Integer price;                       // 물건 가격
	private Integer deliveryTip;                 // 배달팁
	private String content;                      // 게시글 내용
	private RequesterLocation requesterLocation; // 심부름 요청자의 위치
	private MarketLocation marketLocation;       // 가게 위치
	private Integer deliveryToRequesterDistance; // 배달하는 사람과 배달받을 사람 사이의 거리(미터)
	private Integer deliveryToMarketDistance;    // 배달하는 사람과 가게 사이의 거리(미터)
	private String deliverymanNickname;          // 배달원 닉네임
	private String regdate;                      // 게시글 등록 시간
	private Integer state;                       // 0: 대기중, 1: 배달중, 2: 배달완료
}