package com.example.dizserver.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ChatRoomDto {

    private String id;

    private String chatRoomId;

    private String userId;
    private String userIdName;

    private Date createdAt;
}
