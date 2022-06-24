package com.federicoioan.alternativeschool.service.IService;

import com.federicoioan.alternativeschool.model.ChatMessage;
import com.federicoioan.alternativeschool.model.dto.ChatMessageDto;

import java.util.List;


public interface ChatMessageService {

    List<ChatMessage> findChat(Long senderId, Long receiverId);

    ChatMessage findChatMessage(Long id);

    ChatMessage sendMessage(Long senderId, Long receiverId, ChatMessageDto message);

    // Delete all messages for both users
    void deleteChat(Long senderId, Long receiverId);
}
