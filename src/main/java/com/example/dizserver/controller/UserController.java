package com.example.dizserver.controller;


import com.example.dizserver.dto.Profile;
import com.example.dizserver.dto.ProfileUpdateRequest;
import com.example.dizserver.dto.SearchSend;
import com.example.dizserver.model.User;
import com.example.dizserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/profile_update")
    public ResponseEntity<ProfileUpdateRequest> getProfileUpdate(@RequestParam String id) {
        ProfileUpdateRequest profileUpdateRequest = userService.getProfileUpdateRequest(id);
        return ResponseEntity.ok(profileUpdateRequest);
    }

    @PutMapping("/profile_update")
    public ResponseEntity<ProfileUpdateRequest> updateUser(@RequestParam String id, @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        return ResponseEntity.ok(userService.updateUser(id, profileUpdateRequest));
    }


    @PutMapping("/follow")
    public ResponseEntity follow(@RequestParam String idUser, @RequestParam String idFollow) {
        userService.userFollow(idUser, idFollow);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unfollow")
    public ResponseEntity unfollow(@RequestParam String idUser, @RequestParam String idFollow) {
        userService.userUnfollow(idUser, idFollow);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/profile/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getProfileById(id));
    }


    @GetMapping("/isPostMark")
    public ResponseEntity<Boolean> isPostMark(@RequestParam(required = false) String idUser,
                                              @RequestParam(required = false) String idPost
    ) {
        return ResponseEntity.ok(userService.isPostMark(idUser, idPost));
    }

    @GetMapping("/search")
    public SearchSend searchUsers(@RequestParam(required = false) String id,
                                  @RequestParam(required = false) String userName,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {

        return userService.searchUser(id, userName, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
