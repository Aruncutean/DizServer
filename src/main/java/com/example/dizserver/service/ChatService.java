package com.example.dizserver.service;

import com.example.dizserver.dto.ChatRoomDto;
import com.example.dizserver.dto.MessageDto;
import com.example.dizserver.model.ChatMessage;
import com.example.dizserver.model.ChatRoom;
import com.example.dizserver.model.User;
import com.example.dizserver.repository.ChatMessageRepository;
import com.example.dizserver.repository.ChatRoomRepository;
import com.example.dizserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    public String makeChat(String userOneId, String userTwoId) {
        User userOne = userRepository.findById(userOneId).orElseThrow(() -> new NoSuchElementException("User not found with id: " + userOneId));
        User userTow = userRepository.findById(userTwoId).orElseThrow(() -> new NoSuchElementException("User not found with id: " + userTwoId));

        String chatRoomId = generateChatRoomId(userOneId, userTwoId);

        if (userOne.getChatRoomId() == null) {
            userOne.setChatRoomId(new ArrayList<>());
        }
        if (userTow.getChatRoomId() == null) {
            userTow.setChatRoomId(new ArrayList<>());
        }

        if (!userOne.getChatRoomId().contains(chatRoomId)) {
            userOne.getChatRoomId().add(chatRoomId);
        }

        if (!userTow.getChatRoomId().contains(chatRoomId)) {
            userTow.getChatRoomId().add(chatRoomId);
        }


        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomId(chatRoomId);
        chatRoom.setUserIdOne(userOneId);
        chatRoom.setUserIdTwo(userTwoId);
        chatRoom.setCreatedAt(new Date());

        chatRoomRepository.save(chatRoom);
        userRepository.save(userOne);
        userRepository.save(userTow);

        return chatRoomId;

    }

    public void deleteMessage(String messageID){
        ChatMessage chatMessage= chatMessageRepository.findById(messageID).get();
        chatMessage.setIsDeleted(true);

        chatMessageRepository.save(chatMessage);

    }


    public void saveMessage(MessageDto message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomId(message.getChatRoomId());
        chatMessage.setText(message.getText());
        chatMessage.setCreatedAt(new Date());
        chatMessage.setFromUserID(message.getFromUserID());

        chatMessageRepository.save(chatMessage);
    }

    public List<MessageDto> getChatMessage(String chatRoomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIdInOrderByCreatedAtDesc(chatRoomId, pageable);

        List<MessageDto> messageDtos = new ArrayList<>();

        for (ChatMessage chatMessage : chatMessages) {
            MessageDto messageDto = new MessageDto();
            messageDto.setId(chatMessage.getId());
            messageDto.setFromUserID(chatMessage.getFromUserID());
            messageDto.setChatRoomId(chatMessage.getChatRoomId());
            messageDto.setText(chatMessage.getText());
            messageDto.setCreatedAt(chatMessage.getCreatedAt());

            messageDtos.add(messageDto);
        }

        return messageDtos;
    }

    public List<ChatRoomDto> getChatList(String userId, int page, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        if (user.getChatRoomId() == null) {
            user.setChatRoomId(new ArrayList<>());
        }
        Pageable pageable = PageRequest.of(page, size);

        List<ChatRoom> chatRoomList = chatRoomRepository.findByChatRoomIdInOrderByCreatedAtDesc(user.getChatRoomId(), pageable);
        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomList) {
            ChatRoomDto chatRoomDto = new ChatRoomDto();
            chatRoomDto.setChatRoomId(chatRoom.getChatRoomId());

            if (!chatRoom.getUserIdOne().equals(userId)) {
                User user2 = userRepository.findById(chatRoom.getUserIdOne()).orElseThrow(() -> new NoSuchElementException("User not found with id: " + chatRoom.getUserIdOne()));

                chatRoomDto.setUserId(chatRoom.getUserIdOne());
                chatRoomDto.setUserIdName(user2.getUserName());
            }
            if (!chatRoom.getUserIdTwo().equals(userId)) {
                User user2 = userRepository.findById(chatRoom.getUserIdTwo()).orElseThrow(() -> new NoSuchElementException("User not found with id: " + chatRoom.getUserIdTwo()));
                chatRoomDto.setUserId(chatRoom.getUserIdTwo());
                chatRoomDto.setUserIdName(user2.getUserName());
            }

            chatRoomDto.setCreatedAt(chatRoom.getCreatedAt());
            chatRoomDtoList.add(chatRoomDto);
        }

        return chatRoomDtoList;

    }


    public String generateChatRoomId(String userId1, String userId2) {
        String[] userIds = {userId1, userId2};
        Arrays.sort(userIds);

        return userIds[0] + "_" + userIds[1];
    }
}
