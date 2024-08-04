package com.example.dizserver.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Profile {
    private String userName;

    private String name;

    private String profileImg;

    private String bio;

    private Integer posts;
    private Integer followers;
    private Integer following;
}
