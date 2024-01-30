package com.example.shopperman.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopperman.dao.MemberDao;
import com.example.shopperman.entity.LoginForm;
import com.example.shopperman.entity.Member;

@Repository("memberDao")
@Transactional
public class MybatisMemberDao implements MemberDao {

    private MemberDao mapper;

    @Autowired
    public MybatisMemberDao(SqlSession sqlSession) {
        mapper = sqlSession.getMapper(MemberDao.class);
    }


    @Override
    public Member getMemberByLoginForm(LoginForm loginForm) {
        return mapper.getMemberByLoginForm(loginForm);
    }
    
    @Override
	public Member getMemberById(String id) {
		return mapper.getMemberById(id);
	}
    
    @Override
	public List<Member> getMemberList() {
		return mapper.getMemberList();
	}

    @Override
    public boolean insertMember(Member member) {
        return mapper.insertMember(member);
    }

    @Override
    public String getIdById(String id) {
        return mapper.getIdById(id);
    }

    @Override
    public String getIdByNickname(String nickname) {
        return mapper.getIdByNickname(nickname);
    }
    
	@Override
	public Integer getSumScore(String id) {
		return mapper.getSumScore(id);
	}

	@Override
	public Integer getNumScore(String id) {
		return mapper.getNumScore(id);
	}

	@Override
	public boolean setScore(Map<String, Object> newScore) {
		return mapper.setScore(newScore);
	}
	
	@Override
	public Integer getPoint(String id) {
		return mapper.getPoint(id);
	}

	@Override
	public boolean subtractPoint(Map<String, Object> idAndPoint) {
		return mapper.subtractPoint(idAndPoint);
	}

	@Override
	public boolean addPoint(Map<String, Object> nicknameAndPoint) {
		return mapper.addPoint(nicknameAndPoint);
	}
}