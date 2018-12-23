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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class MessageController {

    @Resource
    private MessageRepository messageRepo;

    @Resource
    private EntityManager em;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private Properties properties;

    @Transactional
    @RequestMapping("/receive-message")
    public void receiveMessage(HttpServletRequest request) throws IOException, URISyntaxException {
        System.out.println("Base URL");
        System.out.println(properties.getBaseUrl());
        try {
            Message message = new Message(request);
            long id = messageRepo.save(message).getId();
            em.flush();
            em.clear();
            message = messageRepo.findById(id).get();
            Group group = message.getGroup();
            Bot bot = group.getBot();
            if (bot == null) {
                URI uri = new URI(new StringBuilder()
                        .append(properties.getBaseUrl())
                        .append("/bots?token=")
                        .append(properties.getAccessToken()).toString());
                List<Object> results = restTemplate.getForObject(uri, List.class);
                System.out.println(results);
            }
//            em.flush();
//            em.clear();
//            message = messageRepo.findById(id).get();
//            String text = message.getText();
//            System.out.println(text);
//            String botId = message.getGroup().getBot().getId();
//            System.out.println(botId);
//            sendMessage(text, botId);
        } catch (BotMessageException e) {
            System.out.println("Message was from Bot");
        } catch (SystemMessageException e) {
            System.out.println("Message was from System");
        }
    }

    List<Object> getBotsAndAttachToGroups() throws URISyntaxException {
        URI uri = new URI(properties.getBaseUrl() + "/bots");
        List<Object> results = restTemplate.getForObject(uri, List.class);
        return results;
    }

    private void sendMessage(String text, String botId) {
        BotMessage botMessage = new BotMessage(text, botId);
        HttpEntity<BotMessage> entity = new HttpEntity<>(botMessage);
        String botMessageUrl = properties.getBaseUrl() + "/bots/post";
        restTemplate.postForLocation(botMessageUrl, entity);
    }

    @GetMapping("/messages")
    public Iterable<Message> getMessages() {
        return messageRepo.findAll();
    }
}
