package com.example.dizserver.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "chatRoom")
public class ChatRoom {

    @Id
    private String id;

    private String chatRoomId;
    private String userIdOne;
    private String userIdTwo;

    private Date createdAt;


}
