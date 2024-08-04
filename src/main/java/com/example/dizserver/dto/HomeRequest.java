package com.example.dizserver.dto;

import com.example.dizserver.model.Comment;
import com.example.dizserver.model.ImageType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
public class HomeRequest {

    private String id;
    private String userId;
    private String userName;
    private String image;
    private String bio;
    private List<Comment> commentList;
    private List<String> like;
    private Integer likeNr;
    private Date createdAt;
}
