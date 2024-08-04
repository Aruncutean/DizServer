package com.example.dizserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "posts")
public class Post {
     @Id
     private String id;

     private String userId;

     private String image;

     private String bio;

     private List<Comment> commentList;

     private List<String> like;

     private Date createdAt;
}
