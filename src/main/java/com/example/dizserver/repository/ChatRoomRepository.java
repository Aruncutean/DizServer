package com.example.dizserver.repository;

import com.example.dizserver.model.ChatRoom;
import com.example.dizserver.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    List<ChatRoom> findByChatRoomIdInOrderByCreatedAtDesc(List<String> chatRoomId, Pageable pageable);
}
