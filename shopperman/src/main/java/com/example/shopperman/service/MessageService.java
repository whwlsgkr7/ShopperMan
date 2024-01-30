package com.example.shopperman.service;

import java.util.List;

import com.example.shopperman.entity.Message;

public interface MessageService {

	boolean sendNewMessage(Message message);
	
	boolean sendMessage(Message message);
	
	List<List<Message>> getMessageLists(String currentUserNickname);
	
	List<Message> getMessageListById(Integer messageId);
	
	Integer readMessage(String currentUserNickname, Integer messageId);
	
	Integer getUnreadMessageCountForAllPost(String currentUserNickname);
	
	Integer getUnreadMessageCountForPost(String currentUserNickname, Integer messageId);

	String getOpponentNickname(String currentUserNickname, Integer messageId);

	Integer getPostIdByMessageId(Integer messageId);
}
