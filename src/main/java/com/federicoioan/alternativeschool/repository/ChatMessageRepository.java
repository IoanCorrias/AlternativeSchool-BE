package com.federicoioan.alternativeschool.repository;

import com.federicoioan.alternativeschool.model.ChatMessage;
import com.federicoioan.alternativeschool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findChatMessagesBySenderAndReceiver(User sender, User receiver);
    void deleteAllBySenderAndReceiver(User sender, User receiver);
}
