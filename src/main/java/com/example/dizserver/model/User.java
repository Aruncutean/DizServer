package com.example.dizserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.time.LocalDate;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private String userName;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String profileImg;

    private List<String> followers;

    private List<String> following;

    private List<String> postMark;

    private LocalDate birthDate;

    private String password;

    private List<String> chatRoomId;

    private String bio;
    private String gender;
}
