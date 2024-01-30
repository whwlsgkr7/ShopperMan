package com.example.shopperman.service;

import java.util.List;

import com.example.shopperman.entity.LoginForm;
import com.example.shopperman.entity.Member;

public interface MemberService {

    // 로그인 시에 Member 객체 가져오기
    Member getMemberToLogin(LoginForm loginForm);
    
    // id로 Member 객체 가져오기
    Member getMemberById(String id);
    
    // Member List 가져오기
    List<Member> getMemberList();

    // 회원가입
    int join(Member member);

    // 아이디 중복체크
    boolean checkIdDuplicate(String id);

    // 닉네임 중복체크
    boolean checkNicknameDuplicate(String nickname);
    
    // 평점 가져오기
    Double getRating(String id);
    
    // 평점 매기기
    boolean setScore(String id, Integer score);
    
    // 포인트 빼기
    String subtractPoint(String id, Integer point);
    
    // 포인트 더하기
    boolean addPoint(String nickname, Integer point);
}