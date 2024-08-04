package com.example.dizserver.model;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private String id;
    private String userId;
    private String userName;
    private String comment;
    private Date createdAt;
}
