package com.example.dizserver.service;

import com.example.dizserver.dto.CommentDto;
import com.example.dizserver.dto.HomeRequest;
import com.example.dizserver.model.Comment;
import com.example.dizserver.model.Post;
import com.example.dizserver.model.User;
import com.example.dizserver.repository.PostRepository;
import com.example.dizserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    public HomeRequest getPost(String postId){
        Post post = postRepository.findById(postId).get();
        User userPost = userRepository.findById(post.getUserId()).orElseThrow(() -> new NoSuchElementException("User not found with id: " + post.getUserId()));
        HomeRequest homeRequest = new HomeRequest();
        homeRequest.setId(post.getId());
        homeRequest.setBio(post.getBio());
        homeRequest.setImage(post.getImage());
        homeRequest.setUserId(post.getUserId());
        homeRequest.setCommentList(post.getCommentList());
        homeRequest.setLike(post.getLike());
        homeRequest.setUserName(userPost.getUserName());
        homeRequest.setCreatedAt(post.getCreatedAt());

        if (post.getLike() != null) {
            homeRequest.setLikeNr(post.getLike().size());
        } else {
            homeRequest.setLikeNr(0);
        }

        return homeRequest;
    }

    public List<Post> getPostsByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByUserId(userId, pageable);
    }

    public List<Post> getPostsByUserIds(List<String> userIds, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByUserIds(userIds, pageable);
    }


    public List<HomeRequest> getPostForHomePage(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        List<String> userF = user.getFollowing();
        List<Post> posts = postRepository.findByUserIdInOrderByCreatedAtDesc(userF, pageable);
        List<HomeRequest> homeRequests = new ArrayList<>();
        for (Post post : posts) {
            User userPost = userRepository.findById(post.getUserId()).orElseThrow(() -> new NoSuchElementException("User not found with id: " + post.getUserId()));
            HomeRequest homeRequest = new HomeRequest();
            homeRequest.setId(post.getId());
            homeRequest.setBio(post.getBio());
            homeRequest.setImage(post.getImage());
            homeRequest.setUserId(post.getUserId());
            homeRequest.setCommentList(post.getCommentList());
            homeRequest.setLike(post.getLike());
            homeRequest.setUserName(userPost.getUserName());
            homeRequest.setCreatedAt(post.getCreatedAt());
            if (post.getLike() != null) {
                homeRequest.setLikeNr(post.getLike().size());
            } else {
                homeRequest.setLikeNr(0);
            }

            homeRequests.add(homeRequest);
        }

        return homeRequests;
    }

    public Integer addLike(String userId, String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("Post not found with id: " + postId));
        if (post.getLike() == null) {
            post.setLike(new ArrayList<>());

        }
        post.getLike().add(userId);

        postRepository.save(post);
        return post.getLike().size();
    }

    public Integer removeLike(String userId, String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("Post not found with id: " + postId));
        if (post.getLike() == null) {
            post.setLike(new ArrayList<>());
        }
        if (post.getLike().contains(userId)) {
            post.getLike().remove(userId);
        }

        postRepository.save(post);
        return post.getLike().size();
    }

    public void addMark(String userId, String postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        if (user.getPostMark() == null) {
            user.setPostMark(new ArrayList<>());

        }
        user.getPostMark().add(postId);

        userRepository.save(user);
    }

    public void deleteMark(String userId, String postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        if (user.getPostMark() == null) {
            user.setPostMark(new ArrayList<>());
        }
        if (user.getPostMark().contains(postId)) {
            user.getPostMark().remove(postId);
        }

        userRepository.save(user);
    }


    public void addComment(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId()).orElseThrow(() -> new NoSuchElementException("Post not found with id: " + commentDto.getPostId()));

        if (post.getCommentList() == null) {
            post.setCommentList(new ArrayList<Comment>());
        }
        Comment comment = new Comment();
        comment.setUserId(generateUniqueId());
        comment.setComment(commentDto.getComment());
        comment.setUserId(commentDto.getUserId());
        comment.setUserName(commentDto.getUserName());
        comment.setCreatedAt(new Date());
        post.getCommentList().add(comment);

        postRepository.save(post);
    }

    public List<CommentDto> getComments(String postId, int page, int size) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("Post not found with id: " + postId));
        List<CommentDto> comments = new ArrayList<>();
        if (post.getCommentList() == null) {
            post.setCommentList(new ArrayList<Comment>());
        }
        List<Comment> sortedComments = post.getCommentList().stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .collect(Collectors.toList());
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, sortedComments.size());

        if (fromIndex > sortedComments.size()) {
            return comments;
        }

        if(fromIndex<0){
            return comments;
        }
        List<Comment> listP = sortedComments.subList(fromIndex, toIndex);


        for (Comment comment : listP) {
            CommentDto commentDto = new CommentDto();
            commentDto.setComment(comment.getComment());
            commentDto.setUserId(comment.getUserId());
            commentDto.setUserName(comment.getUserName());
            commentDto.setPostId(post.getId());
            commentDto.setCreatedAt(post.getCreatedAt());

            comments.add(commentDto);
        }


        return comments;
    }


    public void removeComment(String commentId, String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("Post not found with id: " + postId));
        if (post.getCommentList() == null) {
            post.setCommentList(new ArrayList<>());
        }
        int index = -1;
        for (int i = 0; i < post.getCommentList().size(); i++) {
            if (post.getCommentList().get(i).getId().equals(commentId)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            post.getCommentList().remove(index);
        }

        postRepository.save(post);
    }

    public void getMark(String userId) {

    }


    public static String generateUniqueId() {

        String timestamp = Instant.now().toString();
        String uuid = UUID.randomUUID().toString();
        String uniqueId = timestamp + "-" + uuid;
        return uniqueId;
    }


}
