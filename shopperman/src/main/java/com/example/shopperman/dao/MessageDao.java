package com.example.shopperman.dao;

import java.util.List;
import java.util.Map;

import com.example.shopperman.entity.Message;

public interface MessageDao {

	boolean insertNewMessage(Message message);
	
	boolean insertMessage(Message message);

	List<Integer> getMessageIdListByUserNickname(String currentUserNickname);
	
	List<Message> getMessageListById(Integer messageId);

	Integer updateReadStatusToRead(Map<String, Object> currentUserNicknameAndMessageId);

	Integer getUnreadMessageCountForAllPost(String currentUserNickname);

	Integer getUnreadMessageCountForPost(Map<String, Object> currentUserNicknameAndMessageId);

	String getSenderNickname(Map<String, Object> currentUserNicknameAndMessageId);
	
	String getRecipientNickname(Map<String, Object> currentUserNicknameAndMessageId);

	Integer getPostIdByMessageId(Integer messageId);
}
