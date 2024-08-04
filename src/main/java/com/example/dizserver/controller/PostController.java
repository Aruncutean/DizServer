package com.example.dizserver.controller;

import com.example.dizserver.dto.CommentDto;
import com.example.dizserver.dto.HomeRequest;
import com.example.dizserver.model.Post;
import com.example.dizserver.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable String userId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        List<Post> posts = postService.getPostsByUserId(userId, page, size);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/getPost")
    public ResponseEntity<HomeRequest> getPost(@RequestParam String postId) {

        return ResponseEntity.ok(postService.getPost(postId));
    }


    @GetMapping
    public List<Post> getPosts(@RequestParam List<String> userIds,
                               @RequestParam int page,
                               @RequestParam int size) {
        return postService.getPostsByUserIds(userIds, page, size);
    }

    @GetMapping("/getHomePost")
    public List<HomeRequest> getPosts(@RequestParam String userId,
                                      @RequestParam int page,
                                      @RequestParam int size) {
        return postService.getPostForHomePage(userId, page, size);
    }

    @PutMapping("/addLike")
    public ResponseEntity addLike(@RequestParam String userId,
                                  @RequestParam String postId
    ) {

        return ResponseEntity.ok(postService.addLike(userId, postId));
    }

    @PutMapping("/addMark")
    public ResponseEntity addMark(@RequestParam String userId,
                                  @RequestParam String postId
    ) {
        postService.addMark(userId, postId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/removeMarkPost")
    public ResponseEntity removeMarkPost(@RequestParam String userId,
                                         @RequestParam String postId
    ) {
        postService.deleteMark(userId, postId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/removeLike")
    public ResponseEntity<Integer> removeLike(@RequestParam String userId,
                                              @RequestParam String postId
    ) {

        return ResponseEntity.ok(postService.removeLike(userId, postId));
    }


    @PostMapping("/addComment")
    public ResponseEntity addComment(@RequestBody CommentDto commentDto) {
        postService.addComment(commentDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getComments")
    public ResponseEntity<List<CommentDto>> getComments(@RequestParam String postId,
                                                        @RequestParam int page,
                                                        @RequestParam int size) {
        return ResponseEntity.ok(postService.getComments(postId, page, size));
    }


}
