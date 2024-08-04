package com.example.dizserver.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProfileUpdateRequest {

    private String userName;

    private String name;

    private String email;

    private String profileImg;

    private LocalDate birthDate;

    private String gender;

    private String bio;

}
