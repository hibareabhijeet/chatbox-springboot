package com.abhi.chatbox.controller;

import com.abhi.chatbox.service.MessageService;
import com.abhi.chatbox.utils.Message;
import com.abhi.chatbox.utils.OutputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class MessageHandlingController {

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

    private final MessageService messageService;

    public MessageHandlingController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message) {

        Message savedMessage = messageService.saveMessage(message);

        return new OutputMessage(
                savedMessage.from(),
                savedMessage.text(),
                LocalTime.now().format(TIME_FORMATTER));
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();

        return ResponseEntity.ok(messages);
    }
}
