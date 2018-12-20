package com.alexjamesmalcolm.dishbot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MessageController {

    @Resource
    private MessageRepository messageRepo;

    @RequestMapping("/receive-message")
    public void receiveMessage(Message message) {
        messageRepo.save(message);
    }

    @GetMapping("/messages")
    public Iterable<Message> getMessages() {
        Iterable<Message> messages = messageRepo.findAll();
        return messages;
    }
}
