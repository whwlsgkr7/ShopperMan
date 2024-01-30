package com.example.shopperman.entity;

import lombok.Data;

@Data
public class Message {
	private Integer messageId;
    private String senderNickname;
    private String recipientNickname;
    private String content;
    private Integer postId;
    private String regdate;
    private Integer readStatus;
}