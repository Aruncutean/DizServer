package com.example.dizserver.dto;

import lombok.Data;

@Data
public class CommentRequest {
    String commentId;
    String userId;
    String comment;
    String postId;
}
