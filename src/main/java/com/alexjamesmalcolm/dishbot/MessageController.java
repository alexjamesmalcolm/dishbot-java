package com.alexjamesmalcolm.dishbot;

import org.springframework.http.HttpEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class MessageController {

    @Resource
    private MessageRepository messageRepo;

    @Resource
    private EntityManager em;

    @Resource
    private RestTemplate restTemplate;

    private String botMessageUrl = "https://api.groupme.com/v3/bots/post";

    @Transactional
    @RequestMapping("/receive-message")
    public void receiveMessage(HttpServletRequest request) throws IOException, SystemMessageException {
        Message message = new Message(request);
        long id = messageRepo.save(message).getId();
        em.flush();
        em.clear();
        message = messageRepo.findById(id).get();
        String text = message.getText();
        System.out.println(text);
        String botId = message.getGroup().getBotId();
        System.out.println(botId);
        BotMessage botMessage = new BotMessage(text, botId);
        HttpEntity<BotMessage> entity = new HttpEntity<>(botMessage);
        restTemplate.postForLocation(botMessageUrl, entity);
    }

    @GetMapping("/messages")
    public Iterable<Message> getMessages() {
        Iterable<Message> messages = messageRepo.findAll();
        return messages;
    }
}
