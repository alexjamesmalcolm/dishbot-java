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
import java.util.Map;

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

    @Resource
    private BotRepository botRepo;

    @Transactional
    @RequestMapping("/receive-message")
    public void receiveMessage(HttpServletRequest request) throws IOException, URISyntaxException {
        try {
            Message message = new Message(request);
            long id = messageRepo.save(message).getId();
            em.flush();
            em.clear();
            message = messageRepo.findById(id).get();
            long groupId = message.getGroup().getId();
            Bot bot = message.getGroup().getBot();
            if (bot == null) {
                URI uri = new URI(new StringBuilder()
                        .append(properties.getBaseUrl())
                        .append("/bots?token=")
                        .append(properties.getAccessToken()).toString());
                Map<String, Object> results = restTemplate.getForObject(uri, Map.class);
                System.out.println(results);
                List<Map> bots = (List<Map>) results.get("response");
                // [{name=Snackatron, bot_id=e3af5b0fbe6a6be70d16dbd9fc, group_id=37820787, group_name=Guinness VI Squad, avatar_url=null, callback_url=, dm_notification=false}, {name=Test bot, bot_id=6dc7db8c212c036ddc9dc9acbc, group_id=46707218, group_name=Empty Bot Test Group, avatar_url=null, callback_url=https://dishbot.herokuapp.com/receive-message, dm_notification=false}]
                System.out.println(bots);
                bot = bots.stream().map(botMap -> {
                    String bot_id = (String) botMap.get("bot_id");
                    String name = (String) botMap.get("name");
                    long group_id = Long.parseLong((String) botMap.get("group_id"));
                    Group group = new Group(group_id);
                    return new Bot(bot_id, name, group);
                }).filter(b -> {
                    return b.getGroup().getId() == groupId;
                }).findFirst().get();
                System.out.println(bot);
                bot = botRepo.save(bot);
                System.out.println(bot);
            }
            em.flush();
            em.clear();
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
