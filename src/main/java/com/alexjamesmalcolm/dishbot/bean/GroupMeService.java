package com.alexjamesmalcolm.dishbot.bean;

import com.alexjamesmalcolm.dishbot.Properties;
import com.alexjamesmalcolm.dishbot.groupme.Bot;
import com.alexjamesmalcolm.dishbot.groupme.Group;
import com.alexjamesmalcolm.dishbot.groupme.Member;
import com.alexjamesmalcolm.dishbot.groupme.Message;
import com.alexjamesmalcolm.dishbot.logical.BotMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class GroupMeService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private Properties properties;

    public Group getGroup(Long groupId) {
        String path = properties.getBaseUrl() + "/groups/" + groupId + "?token=" + properties.getAccessToken();
        Map json = restTemplate.getForObject(path, Map.class);
        Group group = objectMapper.convertValue(json.get("response"), Group.class);
        return group;
    }

    public List<Message> getMessages(Long groupId) {
        String path = properties.getBaseUrl() + "/groups/" + groupId + "/messages?token=" + properties.getAccessToken();
        Map json = restTemplate.getForObject(path, Map.class);
        Map response = (Map) json.get("response");
        Message[] messages = objectMapper.convertValue(response.get("messages"), Message[].class);
        return Arrays.asList(messages);
    }

    public Bot getBot(Long groupId) {
        List<Bot> bots = getAllBots();
        return bots.stream().filter(bot -> bot.getGroup_id().equals(groupId)).findFirst().get();
    }

    public List<Bot> getAllBots() {
        String path = properties.getBaseUrl() + "/bots?token=" + properties.getAccessToken();
        Map json = restTemplate.getForObject(path, Map.class);
        Bot[] bots = objectMapper.convertValue(json.get("response"), Bot[].class);
        return Arrays.asList(bots);
    }

    public List<Group> getAllGroups() {
        String path = properties.getBaseUrl() + "/groups?token=" + properties.getAccessToken();
        Map json = restTemplate.getForObject(path, Map.class);
        Group[] groups = objectMapper.convertValue(json.get("response"), Group[].class);
        return Arrays.asList(groups);
    }

    public Member getMember(Message message) {
        // TODO Get the member who said this message
        return null;
    }

    public void sendMessage(BotMessage message) {
        HttpEntity<BotMessage> entity = new HttpEntity<>(message);
        String botMessageUrl = properties.getBaseUrl() + "/bots/post";
        restTemplate.postForLocation(botMessageUrl, entity);
    }

    public void sendMessage(String text, String botId) {
        BotMessage botMessage = new BotMessage(text, botId);
        sendMessage(botMessage);
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        if (this.restTemplate == null) {
            this.restTemplate = restTemplate;
        }
    }
}
