package com.example.dizserver.repository;

import com.example.dizserver.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUserId(String userId, Pageable pageable);
    List<Post> findByUserId(String userId);

    @Query("{ 'userId': { $in: ?0 } }")
    List<Post> findByUserIds(List<String> userIds, Pageable pageable);

    List<Post> findByUserIdInOrderByCreatedAtDesc(List<String> userIds, Pageable pageable);


}
