package com.federicoioan.alternativeschool.service;

import com.federicoioan.alternativeschool.model.ChatMessage;
import com.federicoioan.alternativeschool.model.User;
import com.federicoioan.alternativeschool.model.dto.ChatMessageDto;
import com.federicoioan.alternativeschool.repository.ChatMessageRepository;
import com.federicoioan.alternativeschool.repository.UserRepository;
import com.federicoioan.alternativeschool.service.IService.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ChatMessage> findChat(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        List<ChatMessage> sendedMessages = chatMessageRepository.findChatMessagesBySenderAndReceiver(sender, receiver);

        List<ChatMessage> receivedMessages = chatMessageRepository.findChatMessagesBySenderAndReceiver(receiver, sender);

        sendedMessages.addAll(receivedMessages);

        sendedMessages.sort(Comparator.comparing(ChatMessage::getDateInsert));

        return sendedMessages;
    }

    public ChatMessage findChatMessage(Long id) {
        return chatMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat message not found"));
    }

    public ChatMessage sendMessage(Long senderId, Long receiverId, ChatMessageDto message) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setReceiver(receiver);
        chatMessage.setMessage(message.getMessage());

        return chatMessageRepository.save(chatMessage);
    }

    public void deleteChat(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        chatMessageRepository.deleteAllBySenderAndReceiver(sender, receiver);
    }
}