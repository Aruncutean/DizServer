package com.example.dizserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "chatMessage")
public class ChatMessage {

    @Id
    private String id;

    private String fromUserID;
    private String text;
    private String chatRoomId;
    private Date createdAt;
    private Boolean isDeleted = false;
}
