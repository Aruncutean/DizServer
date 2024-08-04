package com.example.dizserver.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {
    String userId;
    String userName;
    String postId;
    String comment;
     Date createdAt;
}
