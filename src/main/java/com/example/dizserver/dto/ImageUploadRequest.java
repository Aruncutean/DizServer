package com.example.dizserver.dto;

import com.example.dizserver.model.ImageType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUploadRequest {

    private String userId;
    private ImageType imageType;
    private MultipartFile file;
    private String bio;

}
