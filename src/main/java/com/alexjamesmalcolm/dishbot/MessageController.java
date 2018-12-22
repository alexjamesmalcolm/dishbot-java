package com.alexjamesmalcolm.dishbot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class MessageController {

    @Resource
    private MessageRepository messageRepo;

    @RequestMapping("/receive-message")
    public void receiveMessage(HttpServletRequest request) throws IOException {
        Message message = new Message(request);
        messageRepo.save(message);
    }

    @GetMapping("/messages")
    public Iterable<Message> getMessages() {
        Iterable<Message> messages = messageRepo.findAll();
        return messages;
    }
}
