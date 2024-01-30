package com.example.shopperman.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopperman.dao.MessageDao;
import com.example.shopperman.entity.Message;

@Repository("messageDao")
@Transactional
public class MybatisMessageDao implements MessageDao {

	private MessageDao mapper;
	
	@Autowired
    public MybatisMessageDao(SqlSession sqlSession) {
        mapper = sqlSession.getMapper(MessageDao.class);
    }
	
	//---------------------------------------------------------------------------------
	
	@Override
	public boolean insertNewMessage(Message message) {
		return mapper.insertNewMessage(message);
	}
	
	@Override
	public boolean insertMessage(Message message) {
		return mapper.insertMessage(message);
	}

	@Override
	public List<Integer> getMessageIdListByUserNickname(String currentUserNickname) {
		return mapper.getMessageIdListByUserNickname(currentUserNickname);
	}
	
	@Override
	public List<Message> getMessageListById(Integer messageId) {
		return mapper.getMessageListById(messageId);
	}

	@Override
	public Integer updateReadStatusToRead(Map<String, Object> currentUserNicknameAndMessageId) {
		return mapper.updateReadStatusToRead(currentUserNicknameAndMessageId);
	}

	@Override
	public Integer getUnreadMessageCountForAllPost(String currentUserNickname) {
		return mapper.getUnreadMessageCountForAllPost(currentUserNickname);
	}

	@Override
	public Integer getUnreadMessageCountForPost(Map<String, Object> currentUserNicknameAndMessageId) {
		return mapper.getUnreadMessageCountForPost(currentUserNicknameAndMessageId);
	}

	@Override
	public String getSenderNickname(Map<String, Object> currentUserNicknameAndMessageId) {
		return mapper.getSenderNickname(currentUserNicknameAndMessageId);
	}
	
	@Override
	public String getRecipientNickname(Map<String, Object> currentUserNicknameAndMessageId) {
		return mapper.getRecipientNickname(currentUserNicknameAndMessageId);
	}

	@Override
	public Integer getPostIdByMessageId(Integer messageId) {
		return mapper.getPostIdByMessageId(messageId);
	}

}
