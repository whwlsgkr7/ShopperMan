package com.example.shopperman.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopperman.dao.PostDao;
import com.example.shopperman.entity.Post;

@Repository("postDao")
@Transactional
public class MybatisPostDao implements PostDao {

	private PostDao mapper;
	
	@Autowired
    public MybatisPostDao(SqlSession sqlSession) {
        mapper = sqlSession.getMapper(PostDao.class);
    }
	
	// ---------------------------------------------------------------------------------
	
	@Override
	public void insertPost(Post post) {
		mapper.insertPost(post);
	}

	@Override
	public List<Post> getPostList(String currentUserNickname) {
		return mapper.getPostList(currentUserNickname);
	}
	
	@Override
	public List<Post> getPostListByIdList(List<Integer> idList) {
		return mapper.getPostListByIdList(idList);
	}
	
	@Override
	public Post getPost(Integer id) {
		return mapper.getPost(id);
	}

	@Override
	public boolean deletePost(Integer id) {
		return mapper.deletePost(id);
	}

	@Override
	public String getPublisherNicknameByPostId(Integer id) {
		return mapper.getPublisherNicknameByPostId(id);
	}

	@Override
	public boolean setDeliverymanNickname(Map<String, Object> idAndDeliverymanNickname) {
		return mapper.setDeliverymanNickname(idAndDeliverymanNickname);
	}
	
	@Override
	public String getDeliverymanNickname(Integer id) {
		return mapper.getDeliverymanNickname(id);
	}
	
	@Override
	public Integer getState(Integer id) {
		return mapper.getState(id);
	}
	
	@Override
	public boolean setState(Map<String, Integer> idAndState) {
		return mapper.setState(idAndState);
	}

	@Override
	public Integer getPrice(Integer id) {
		return mapper.getPrice(id);
	}

	@Override
	public Integer getDeliveryTip(Integer id) {
		return mapper.getDeliveryTip(id);
	}
	
	@Override
	public String getTitle(Integer id) {
		return mapper.getTitle(id);
	}

	@Override
	public List<Post> getPostListByPublisherNickname(String nickname) {
		return mapper.getPostListByPublisherNickname(nickname);
	}

	@Override
	public List<Post> getPostListByDeliverymanNickname(String nickname) {
		return mapper.getPostListByDeliverymanNickname(nickname);
	}
}
