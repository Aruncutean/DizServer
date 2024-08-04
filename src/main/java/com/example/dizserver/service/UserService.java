package com.example.dizserver.service;

import com.example.dizserver.dto.*;
import com.example.dizserver.model.Post;
import com.example.dizserver.model.User;
import com.example.dizserver.repository.PostRepository;
import com.example.dizserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public SearchSend searchUser(String userId, String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<User> usersPage = userRepository.findByUserNameContainingIgnoreCase(username, pageable);

        List<UserSearch> userSearches = new ArrayList<>();
        for (User user : usersPage) {
            UserSearch userSearch = new UserSearch();
            userSearch.setUserName(user.getUserName());
            userSearch.setId(user.getId());
            userSearch.setName(user.getName());
            userSearch.setProfileImg(user.getProfileImg());

            if (!Objects.equals(userId, user.getId())) {
                userSearches.add(userSearch);
            }
        }
        SearchSend searchSend = new SearchSend();
        searchSend.setUserSearches(userSearches);
        return searchSend;

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public void userFollow(String idUser, String idFollow) {
        User userFollow = userRepository.findById(idFollow).orElseThrow(() -> new NoSuchElementException("User not found with id: " + idFollow));
        User user = userRepository.findById(idUser).orElseThrow(() -> new NoSuchElementException("User not found with id: " + idUser));

        if (userFollow.getFollowers() == null) {
            userFollow.setFollowers(new ArrayList<>());
            userFollow.getFollowers().add(idUser);
        } else {
            if (!userFollow.getFollowers().contains(idUser)) {
                userFollow.getFollowers().add(idUser);
            }
        }

        if (user.getFollowing() == null) {
            user.setFollowing(new ArrayList<>());
            user.getFollowing().add(idFollow);
        } else {
            if (!(user.getFollowing().contains(idFollow))) {
                user.getFollowing().add(idFollow);
            }
        }

        userRepository.save(user);
        userRepository.save(userFollow);
    }

    public void userUnfollow(String idUser, String idFollow) {
        User user = userRepository.findById(idUser).orElseThrow(() -> new NoSuchElementException("User not found with id: " + idUser));
        User userFollow = userRepository.findById(idFollow).orElseThrow(() -> new NoSuchElementException("User not found with id: " + idFollow));

        if (user.getFollowers() == null) {
            user.setFollowers(new ArrayList<>());
        } else {
            if (!user.getFollowers().contains(idFollow)) {
                user.getFollowing().remove(idFollow);
            }
        }

        if (userFollow.getFollowing() != null) {
            userFollow.setFollowing(new ArrayList<>());
        } else {
            if (userFollow.getFollowing().contains(idUser)) {
                userFollow.getFollowing().remove(idUser);
            }

        }
        userRepository.save(userFollow);
        userRepository.save(user);
    }

    public Profile getProfileById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        Profile profile = new Profile();

        profile.setBio(user.getBio());
        profile.setName(user.getName());
        profile.setUserName(user.getUserName());
        profile.setProfileImg(user.getProfileImg());
        if (user.getFollowing() != null) {
            profile.setFollowing(user.getFollowing().size());
        } else {
            profile.setFollowing(0);
        }
        if (user.getFollowers() != null) {
            profile.setFollowers(user.getFollowers().size());
        } else {
            profile.setFollowers(0);
        }

        profile.setPosts(postRepository.findByUserId(user.getId()).size());

        return profile;
    }

    public ProfileUpdateRequest updateUser(String id, ProfileUpdateRequest profileUpdateRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        user.setName(profileUpdateRequest.getName());
        user.setEmail(profileUpdateRequest.getEmail());
        user.setUserName(profileUpdateRequest.getUserName());
        user.setBio(profileUpdateRequest.getBio());
        user.setBirthDate(profileUpdateRequest.getBirthDate());
        user.setGender(profileUpdateRequest.getGender());
        userRepository.save(user);
        return profileUpdateRequest;
    }

    public ProfileUpdateRequest getProfileUpdateRequest(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest();

        profileUpdateRequest.setName(user.getName());
        profileUpdateRequest.setEmail(user.getEmail());
        profileUpdateRequest.setProfileImg(user.getProfileImg());
        profileUpdateRequest.setBio(user.getBio());
        profileUpdateRequest.setUserName(user.getUserName());
        profileUpdateRequest.setGender(user.getGender());
        profileUpdateRequest.setBirthDate(user.getBirthDate());
        return profileUpdateRequest;
    }

    public User createUser(Register register) {
        User user = new User();
        user.setUserName(register.getName());
        user.setEmail(register.getEmail());
        user.setPassword(passwordEncoder.encode(register.getPassword()));

        return userRepository.save(user);
    }

    public boolean isPostMark(String idUser, String idPost) {
        User user = userRepository.findById(idUser).orElseThrow(() -> new NoSuchElementException("User not found with id: " + idUser));

        if (user.getPostMark() == null) {
            user.setPostMark(new ArrayList<>());
        }

        if (user.getPostMark().contains(idPost)) {
            return true;
        }

        return false;
    }

    public User updateUser(String id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

}
