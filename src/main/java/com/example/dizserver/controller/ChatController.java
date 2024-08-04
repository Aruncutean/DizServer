package com.example.dizserver.controller;

import com.example.dizserver.dto.ChatRoomDto;
import com.example.dizserver.dto.MessageDto;
import com.example.dizserver.model.ChatMessage;
import com.example.dizserver.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/messages/" + message.getChatRoomId(), message);
    }

    @GetMapping("/getChatList")
    public ResponseEntity<List<ChatRoomDto>> getChatList(@RequestParam() String userId,
                                                         @RequestParam() int page,
                                                         @RequestParam() int size) {
        return ResponseEntity.ok(chatService.getChatList(userId, page, size));
    }

    @GetMapping("/getChatMessage")
    public ResponseEntity<List<MessageDto>> getChatMessage(@RequestParam() String chatRoomId,
                                                         @RequestParam() int page,
                                                         @RequestParam() int size) {
        return ResponseEntity.ok(chatService.getChatMessage(chatRoomId, page, size));
    }

    @PostMapping("/makeChate")
    public ResponseEntity<String> makeChate(@RequestParam() String userOneId,
                                            @RequestParam() String userTwoId) {
        return ResponseEntity.ok(chatService.makeChat(userOneId, userTwoId));
    }

    @PostMapping("/saveMessage")
    public ResponseEntity saveMessage(@RequestBody MessageDto messageDto) {
        chatService.saveMessage(messageDto);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/deleteMessage")
    public ResponseEntity deleteMessage(@RequestParam String messageId){
        chatService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }

}