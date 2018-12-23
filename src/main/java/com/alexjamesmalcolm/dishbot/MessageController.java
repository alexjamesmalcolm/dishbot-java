package com.alexjamesmalcolm.dishbot;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.awt.image.SurfaceManager;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class MessageController {

    @Resource
    private MessageRepository messageRepo;

    @Resource
    private EntityManager em;

    @Transient
    @RequestMapping("/receive-message")
    @SendTo("https://api.groupme.com/v3/bots/post")
    public BotMessage receiveMessage(HttpServletRequest request) throws IOException, SystemMessageException {
        Message message = new Message(request);
        long id = messageRepo.save(message).getId();
        em.flush();
        em.clear();
        message = messageRepo.findById(id).get();
        return new BotMessage(message.getText(), message.getGroup().getBotId());
    }

    @GetMapping("/messages")
    public Iterable<Message> getMessages() {
        Iterable<Message> messages = messageRepo.findAll();
        return messages;
    }
}
