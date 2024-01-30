package com.example.shopperman.service;

import java.util.Map;

import com.example.shopperman.entity.Member;

public interface SecurityService {

    // 토큰 생성
    String createToken(Member member);
    
    // subject 가져오기
    Map<String, Object> getSubject(String token);
}