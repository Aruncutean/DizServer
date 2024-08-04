package com.example.dizserver.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MessageDto {
    private String id;

    private String fromUserID;
    private String text;
    private String chatRoomId;
    private Date createdAt;

}
