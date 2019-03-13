package com.alexjamesmalcolm.dishbot.bean;

import com.alexjamesmalcolm.dishbot.Properties;
import com.alexjamesmalcolm.dishbot.groupme.*;
import com.alexjamesmalcolm.dishbot.logical.BotMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupMeService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private Properties properties;

    public Group getGroup(Long groupId) {
        String path = properties.getBaseUrl() + "/groups/" + groupId + "?token=" + properties.getGroupMeAccessToken();
        Envelope envelope = makeRequest(path);
        return envelope.getResponse(Group.class);
    }

    public Group getGroup(Message message) {
        long groupId = message.getGroupId();
        return getGroup(groupId);
    }

    public List<Message> getMessages(Long groupId) {
        String path = properties.getBaseUrl() + "/groups/" + groupId + "/messages?token=" + properties.getGroupMeAccessToken();
        Map json = restTemplate.getForObject(path, Map.class);
        Map response = (Map) json.get("response");
        Message[] messages = objectMapper.convertValue(response.get("messages"), Message[].class);
        return Arrays.asList(messages);
    }

    public List<Bot> getBots(Long groupId) {
        List<Bot> bots = getBots();
        return bots.stream().filter(bot -> bot.getGroupId().equals(groupId)).collect(Collectors.toList());
    }

    public List<Bot> getBots() {
        String path = properties.getBaseUrl() + "/bots?token=" + properties.getGroupMeAccessToken();
        Map json = restTemplate.getForObject(path, Map.class);
        Bot[] bots = objectMapper.convertValue(json.get("response"), Bot[].class);
        return Arrays.asList(bots);
    }

    public List<Group> getAllGroups() {
        String path = properties.getBaseUrl() + "/groups?token=" + properties.getGroupMeAccessToken();
        Map json = restTemplate.getForObject(path, Map.class);
        Group[] groups = objectMapper.convertValue(json.get("response"), Group[].class);
        return Arrays.asList(groups);
    }

    public List<Member> getAllMembers() {
        List<Group> groups = getAllGroups();
        return groups.stream().map(Group::getMembers).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public Member getMember(long userId) {
        List<Member> members = getAllMembers();
        return members.stream().filter(member -> member.getUserId() == userId).findFirst().get();
    }

    public Member getMember(Message message) {
        long groupId = message.getGroupId();
        Group group = getGroup(groupId);
        long userId = message.getUserId();
        return group.getMembers().stream().filter(member -> member.getUserId() == userId).findFirst().get();
    }

    public Me getMe() {
        String path = properties.getBaseUrl() + "/users/me?token=" + properties.getGroupMeAccessToken();
        Envelope envelope = makeRequest(path);
        return envelope.getResponse(Me.class);
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

    private Envelope makeRequest(String path) {
        return restTemplate.getForObject(path, Envelope.class);
    }

    public Optional<Bot> getBot(Long groupId, URI callback) {
        List<Bot> bots = getBots(groupId);
        return bots.stream().filter(bot -> bot.getCallbackUrl().equals(callback)).findFirst();
    }
}
