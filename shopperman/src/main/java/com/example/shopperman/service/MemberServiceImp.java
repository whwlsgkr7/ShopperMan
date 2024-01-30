package com.example.shopperman.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopperman.dao.MemberDao;
import com.example.shopperman.entity.LoginForm;
import com.example.shopperman.entity.Member;

@Service("memberService")
@Transactional
public class MemberServiceImp implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member getMemberToLogin(LoginForm loginForm) {
        return memberDao.getMemberByLoginForm(loginForm);
    }
    
    @Override
	public Member getMemberById(String id) {
    	return memberDao.getMemberById(id);
	}
    
    @Override
	public List<Member> getMemberList() {
		return memberDao.getMemberList();
	}

    @Override
    public int join(Member member) {
    	
    	// id 중복이거나, 닉네임 중복인 경우, 회원가입 불가
    	if(checkIdDuplicate(member.getId())){
    		return 1;
    	}
    	else if (checkNicknameDuplicate(member.getNickname())) {
    		return 2;
    	}
    	// id 중복, 닉네임 중복이 모두 아닌 경우, 회원가입 허용
    	else {
    		memberDao.insertMember(member);
    		return 0;
    	}
    }

    @Override
    public boolean checkIdDuplicate(String id) {

        boolean isIdDuplicate;

        // 해당 아이디를 가진 계정이 존재하지 않을 때
        if (memberDao.getIdById(id) == null) {
            isIdDuplicate = false;
        }
        // 해당 아이디를 가진 계정이 존재할 때
        else {
            isIdDuplicate = true;
        }

        return isIdDuplicate;
    }

    @Override
    public boolean checkNicknameDuplicate(String nickname) {

        boolean isNicknameDuplicate;

        // 해당 아이디를 가진 계정이 존재하지 않을 때
        if (memberDao.getIdByNickname(nickname) == null || memberDao.getIdByNickname(nickname).equals("")) {
            isNicknameDuplicate = false;
        }
        // 해당 아이디를 가진 계정이 존재할 때
        else {
            isNicknameDuplicate = true;
        }

        return isNicknameDuplicate;
    }

	@Override
	public Double getRating(String id) {
		
		Double rating = 0.0;
		if(memberDao.getNumScore(id) != 0) {
			rating = Double.valueOf(memberDao.getSumScore(id).doubleValue()) / memberDao.getNumScore(id);
		}
		
		// 소수점 두번째자리까지 반올림
		return Math.round(rating * Math.pow(10, 2)) / Math.pow(10, 2);
	}

	@Override
	public boolean setScore(String id, Integer score) {
		
		// getSumScore
		Integer sumScore = 0;
		if(memberDao.getSumScore(id) != null) {
			sumScore = memberDao.getSumScore(id);
		}
		
		// getNumScore
		Integer numScore = 0;
		if(memberDao.getNumScore(id) != null) {
			numScore = memberDao.getNumScore(id);
		}
		
		Map<String, Object> newScore = new HashedMap<>();
		newScore.put("id", id);
		newScore.put("newSumScore", sumScore + score);
		newScore.put("newNumScore", numScore + 1);
		
		return memberDao.setScore(newScore);
	}

	@Override
	public String subtractPoint(String id, Integer point) {
		
		// 차감하려고 하는 포인트가 현재 포인트보다 많을 경우
		if(memberDao.getPoint(id) < point) {
			return "{\"result\" : \"LACK_OF_POINTS\"}";
		}
		
		Map<String, Object> idAndPoint = new HashedMap<>();
		idAndPoint.put("id", id);
		idAndPoint.put("point", point);
		
		if(memberDao.subtractPoint(idAndPoint) == true) {
			return "{\"result\" : \"SUCCESS\"}";
		}
		else {
			return "{\"result\" : \"DAO_PROBLEM\"}";
		}
	}

	@Override
	public boolean addPoint(String nickname, Integer point) {
		
		Map<String, Object> nicknameAndPoint = new HashedMap<>();
		nicknameAndPoint.put("nickname", nickname);
		nicknameAndPoint.put("point", point);
		
		return memberDao.addPoint(nicknameAndPoint);
	}
}