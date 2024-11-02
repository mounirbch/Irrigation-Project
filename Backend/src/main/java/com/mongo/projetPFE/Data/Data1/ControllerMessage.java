package com.mongo.projetPFE.Data.Data1;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ControllerMessage {
    private MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ControllerMessage(MessageRepository messageRepository, SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public List<MessageEntity> chat(@DestinationVariable String roomId) {
        Iterable<MessageEntity> messageIterable = this.messageRepository.findAll();

        List<MessageEntity> messages = new ArrayList<>();
        messageIterable.forEach(messages::add);

        messages.forEach(System.out::println);

        return messages;
    }
    @PostMapping("/messages")
    public MessageEntity addMessage(@RequestBody MessageEntity message) {
        this.messageRepository.save(message);



        return message;
    }
    @GetMapping("/messages")
    public List<MessageEntity> chat1(@DestinationVariable String roomId) {
        Iterable<MessageEntity> messageIterable = this.messageRepository.findAll();

        List<MessageEntity> messages = new ArrayList<>();
        messageIterable.forEach(messages::add);

        messages.forEach(System.out::println);

        return messages;
    }


}
