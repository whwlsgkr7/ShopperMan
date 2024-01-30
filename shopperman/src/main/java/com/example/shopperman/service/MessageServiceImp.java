package com.example.shopperman.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopperman.dao.MessageDao;
import com.example.shopperman.entity.Message;

@Service("messageService")
@Transactional
public class MessageServiceImp implements MessageService {

	@Autowired
	private MessageDao messageDao;
	
	//---------------------------------------------------------------------------------
	
	public boolean sendNewMessage(Message message) {
		
		return messageDao.insertNewMessage(message);
	}
	
	public boolean sendMessage(Message message) {

		return messageDao.insertMessage(message);
	}

	public List<List<Message>> getMessageLists(String currentUserNickname) {

		List<List<Message>> messageLists = new ArrayList<List<Message>>();
		List<Integer> messageIds = messageDao.getMessageIdListByUserNickname(currentUserNickname);

		for (Integer messageId : messageIds) {
			List<Message> messageList = messageDao.getMessageListById(messageId);
			messageLists.add(messageList);
		}

		return messageLists;
	}
	
	public List<Message> getMessageListById(Integer messageId) {
		return messageDao.getMessageListById(messageId);
	}

	public Integer readMessage(String currentUserNickname, Integer messageId) {
		Map<String, Object> currentUserNicknameAndMessageId = new HashedMap<>();
		currentUserNicknameAndMessageId.put("currentUserNickname", currentUserNickname);
		currentUserNicknameAndMessageId.put("messageId", messageId);
		return messageDao.updateReadStatusToRead(currentUserNicknameAndMessageId);
	}

	public Integer getUnreadMessageCountForAllPost(String currentUserNickname) {
		return messageDao.getUnreadMessageCountForAllPost(currentUserNickname);
	}

	public Integer getUnreadMessageCountForPost(String currentUserNickname, Integer messageId) {
		Map<String, Object> currentUserNicknameAndMessageId = new HashedMap<>();
		currentUserNicknameAndMessageId.put("currentUserNickname", currentUserNickname);
		currentUserNicknameAndMessageId.put("messageId", messageId);
		return messageDao.getUnreadMessageCountForPost(currentUserNicknameAndMessageId);
	}

	@Override
	public String getOpponentNickname(String currentUserNickname, Integer messageId) {
		Map<String, Object> currentUserNicknameAndMessageId = new HashedMap<>();
		currentUserNicknameAndMessageId.put("currentUserNickname", currentUserNickname);
		currentUserNicknameAndMessageId.put("messageId", messageId);
		
		String senderNickname = messageDao.getSenderNickname(currentUserNicknameAndMessageId);
		String recipientNickname = messageDao.getRecipientNickname(currentUserNicknameAndMessageId);
		if(senderNickname != null) {
			return senderNickname;
		}
		else if(recipientNickname != null) {
			return recipientNickname;
		}
		else {
			return null;
		}
	}

	@Override
	public Integer getPostIdByMessageId(Integer messageId) {
		return messageDao.getPostIdByMessageId(messageId);
	}

}
