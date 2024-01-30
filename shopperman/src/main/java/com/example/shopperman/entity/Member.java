package com.example.shopperman.entity;

import lombok.Data;

@Data
public class Member {
    private String id;
    private String pwd;
    private String name;
    private String nickname;
    private String addr;
    private Integer sumScore;      // 평점 총점
	private Integer numScore;      // 평점 갯수
	private Integer point;         // 포인트
}