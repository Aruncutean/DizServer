package com.example.dizserver.service;

import com.example.dizserver.dto.ImageUploadRequest;
import com.example.dizserver.model.ImageType;
import com.example.dizserver.model.Post;
import com.example.dizserver.model.User;
import com.example.dizserver.repository.PostRepository;
import com.example.dizserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.io.IOException;

@Service
public class ImageService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;


    private static final String UPLOAD_DIR = "uploads/";

    public void saveImage(ImageUploadRequest request) throws IOException {

        User user = userRepository.findUserById(request.getUserId()).orElseThrow(() -> new NoSuchElementException("User not found with id: " + request.getUserId()));


        File uploadDir = new File(UPLOAD_DIR + user.getId());
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        MultipartFile file = request.getFile();
        Path filePath = Paths.get(UPLOAD_DIR + user.getId() + "/" + file.getOriginalFilename());

        Files.write(filePath, file.getBytes());

        if (request.getImageType() == ImageType.post) {
            Post post = new Post();
            post.setUserId(user.getId());
            post.setImage(file.getOriginalFilename());
            post.setCreatedAt(new Date());

            postRepository.save(post);

        } else if (request.getImageType() == ImageType.profile) {
            user.setProfileImg(file.getOriginalFilename());
            userRepository.save(user);
        }


    }

    public byte[] getImage(String userId, String imageName) throws IOException {
        Path imagePath = Paths.get("uploads", userId, imageName);
        if (Files.exists(imagePath)) {
            return Files.readAllBytes(imagePath);
        } else {
            throw new IOException("Image not found: " + imageName);
        }

    }

    public byte[] getImage(String userId) throws IOException {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        Path imagePath = Paths.get("uploads", userId, user.getProfileImg());
        if (Files.exists(imagePath)) {
            return Files.readAllBytes(imagePath);
        } else {
            throw new IOException("Image not found: " + user.getProfileImg());
        }

    }

    public String generateUniqueID() {

        Date now = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = sdf.format(now);


        UUID uuid = UUID.randomUUID();


        String uniqueID = timestamp + "-" + uuid.toString();

        return uniqueID;
    }
}