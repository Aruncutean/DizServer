package com.example.dizserver.controller;

import com.example.dizserver.dto.ImageUploadRequest;
import com.example.dizserver.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity uploadImage(@ModelAttribute ImageUploadRequest request) throws IOException {

            imageService.saveImage(request);
            return new ResponseEntity<>( HttpStatus.OK);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<byte[]> getImage(@PathVariable String userId) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/jpeg"); // Sau "image/png" în funcție de formatul imaginii

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(imageService.getImage(userId));
    }

    @GetMapping("/{userId}/images/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String userId, @PathVariable String imageName) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/jpeg"); // Sau "image/png" în funcție de formatul imaginii

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(imageService.getImage(userId, imageName));
    }
}
