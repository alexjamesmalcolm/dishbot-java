package com.alexjamesmalcolm.dishbot.controller;

import com.alexjamesmalcolm.dishbot.bean.Composer;
import com.alexjamesmalcolm.dishbot.bean.GroupMeService;
import com.alexjamesmalcolm.dishbot.groupme.Bot;
import com.alexjamesmalcolm.dishbot.groupme.Group;
import com.alexjamesmalcolm.dishbot.groupme.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rollbar.notifier.Rollbar;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class MessageController {

    @Resource
    private GroupMeService groupMe;

    @Resource
    private Composer composer;

    @Resource
    private ObjectMapper mapper;

    @Resource
    private Rollbar log;

    @Transactional
    @RequestMapping("/receive-message")
    public void receiveMessage(HttpServletRequest request) throws IOException {
        String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Message message = mapper.readValue(json, Message.class);
        Map<String, Object> info = new HashMap<>();
        info.put("json", json);
        info.put("message", message);
        log.info("Received Message", info);
        long groupId = message.getGroupId();
        Optional<String> response = composer.respond(message);
        response.ifPresent(content -> {
            String botId = getBot(groupId).getBotId();
            groupMe.sendMessage(content, botId);
        });
        composer.tryToWarnAll();
    }

    @GetMapping("/message")
    public Iterable<Message> getMessages() {
        List<Group> groups = groupMe.getAllGroups();
        return groups.stream().map(group -> {
            long groupId = group.getGroupId();
            return groupMe.getMessages(groupId);
        }).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private Bot getBot(long groupId) {
        List<Bot> bots = groupMe.getBots(groupId);
        return bots.get(0);
        // TODO Come up with something better than just getting the first bot
    }
}
