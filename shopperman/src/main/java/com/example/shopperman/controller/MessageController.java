package com.example.shopperman.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopperman.entity.Message;
import com.example.shopperman.service.MessageService;
import com.example.shopperman.service.PostService;
import com.example.shopperman.service.SecurityService;

@RestController
@RequestMapping("/msg")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private PostService postService;
	
	//---------------------------------------------------------------------------------

	// 새 메세지 보내기
	@PostMapping("/send/new")
	public String sendNewMessage(@RequestHeader(value = "Authorization") String token, @RequestBody Message message) {
	// 파라미터: postId, content
		
		String currentUserNickname = (String)(String)securityService.getSubject(token).get("nickname");
		if(currentUserNickname == null) {
			return "{\"result\" : \"FAILURE\"}";
		}
		message.setSenderNickname(currentUserNickname);
		
		String recipientNickname = postService.getPublisherNicknameByPostId(message.getPostId());
		if(recipientNickname == null) {
			return "{\"result\" : \"FAILURE\"}";
		}
		message.setRecipientNickname(recipientNickname);
		
		if (messageService.sendNewMessage(message)) {
			return "{\"result\" : \"COMPLETE\"}";
		} else {
			return "{\"result\" : \"FAILURE\"}";
		}
	}
	
	// 메세지 보내기
	@PostMapping("/send")
	public String sendMessage(@RequestHeader(value = "Authorization") String token, @RequestBody Message message) {
	// 파라미터: messageId, content
		
		String currentUserNickname = (String)securityService.getSubject(token).get("nickname");
		if(currentUserNickname == null) {
			return "{\"result\" : \"FAILURE\"}";
		}
		message.setSenderNickname(currentUserNickname);
		
		String recipientNickname = messageService.getOpponentNickname(currentUserNickname, message.getMessageId());
		if(recipientNickname == null) {
			return "{\"result\" : \"FAILURE\"}";
		}
		message.setRecipientNickname(recipientNickname);
		
		Integer postId = messageService.getPostIdByMessageId(message.getMessageId());
		if(postId == null) {
			return "{\"result\" : \"FAILURE\"}";
		}
		message.setPostId(postId);
		
		if (messageService.sendMessage(message)) {
			return "{\"result\" : \"COMPLETE\"}";
		} else {
			return "{\"result\" : \"FAILURE\"}";
		}
	}

	// 현재 로그인된 계정에서, 모든 톡방에 대한 발신,수신 메시지 목록 순서대로 모두 가져오기
	@GetMapping("/get/lists")
	public List<List<Message>> getMessageLists(@RequestHeader(value = "Authorization") String token) {
		String currentUserNickname = (String)securityService.getSubject(token).get("nickname");
		if(currentUserNickname == null) {
			return null;
		}

		return messageService.getMessageLists(currentUserNickname);
	}
	
	// 특정 톡방의 발신,수신 메시지 목록 순서대로 가져오기
	@GetMapping("/get/listById")
	public List<Message> getMessageListById(Integer messageId) {
		return messageService.getMessageListById(messageId);
	}
	
	// 현재 로그인된 계정에서, 특정 톡방에 대한 메세지 읽기
	@GetMapping("/read")
	public String readMessage(@RequestHeader(value = "Authorization") String token, Integer messageId) {
		String currentUserNickname = (String)securityService.getSubject(token).get("nickname");
		if(currentUserNickname == null) {
			return "{\"result\" : \"FAILURE\"}";
		}
		
		Integer readCount = messageService.readMessage(currentUserNickname, messageId);
		if(readCount == null) {
			return "{\"result\" : \"FAILURE\"}";
		}
		
		return "{\"result\" : \""+readCount+"\"}";
	}

	// 현재 로그인된 계정에서, 읽지 않은 메세지 개수 반환 (새로운 메세지가 있다고 표시할 때 쓰일 예정)
	@GetMapping("/unread/countAll")
	public String unreadMessageCountForAllPost(@RequestHeader(value = "Authorization") String token) {
		String currentUserNickname = (String)securityService.getSubject(token).get("nickname");
		if(currentUserNickname == null) {
			return "{\"result\" : \"FAILURE\"}";
		}
		
		Integer unreadCount = messageService.getUnreadMessageCountForAllPost(currentUserNickname);
		if(unreadCount == null) {
			return "{\"result\" : \"FAILURE\"}";
		}
		
		return "{\"result\" : \""+unreadCount+"\"}";
	}

	// 현재 로그인된 계정에서, 특정 톡방에 대한 읽지 않은 메세지 개수 반환 (새로운 메세지가 있다고 표시할 때 쓰일 예정)
	@GetMapping("/unread/count")
	public String unreadMessageCountForPost(@RequestHeader(value = "Authorization") String token, Integer messageId) {
		String currentUserNickname = (String)securityService.getSubject(token).get("nickname");
		if(currentUserNickname == null) {
			return "{\"result\" : \"FAILURE\"}";
		}
		
		Integer unreadCount = messageService.getUnreadMessageCountForPost(currentUserNickname, messageId);
		if(unreadCount == null) {
			return "{\"result\" : \"FAILURE\"}";
		}
		
		return "{\"result\" : \""+unreadCount+"\"}";
	}
}
