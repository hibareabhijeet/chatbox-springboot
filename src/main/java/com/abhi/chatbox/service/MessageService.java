package com.abhi.chatbox.service;

import com.abhi.chatbox.repo.MessageRepo;
import com.abhi.chatbox.utils.Message;
import com.abhi.chatbox.utils.MessageEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MessageService {

    private final MessageRepo messageRepo;

    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Transactional
    public Message saveMessage(Message message) {

        MessageEntity entity = new MessageEntity();

        entity.setSender(message.from());
        entity.setMessage(message.text());
        entity.setCreatedAt(LocalDateTime.now());

        MessageEntity savedEntity = messageRepo.save(entity);

        return convertToMessage(savedEntity);
    }

    public List<Message> getAllMessages() {

        return messageRepo.findAll()
                .stream()
                .map(this::convertToMessage)
                .toList();
    }

    private Message convertToMessage(MessageEntity entity) {

        return new Message(
                entity.getSender(),
                entity.getMessage()
        );
    }
}