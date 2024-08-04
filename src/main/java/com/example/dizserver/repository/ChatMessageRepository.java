package com.example.dizserver.repository;

import com.example.dizserver.model.ChatMessage;
import com.example.dizserver.model.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatRoomIdInOrderByCreatedAtDesc(String chatRoomId, Pageable pageable);
}
