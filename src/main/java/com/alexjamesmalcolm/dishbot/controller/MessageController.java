package com.alexjamesmalcolm.dishbot.controller;

import com.alexjamesmalcolm.dishbot.bean.GroupMeService;
import com.alexjamesmalcolm.dishbot.bean.Interpreter;
import com.alexjamesmalcolm.dishbot.groupme.Bot;
import com.alexjamesmalcolm.dishbot.groupme.Group;
import com.alexjamesmalcolm.dishbot.groupme.Message;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MessageController {

    @Resource
    private GroupMeService groupMe;

    @Resource
    private Interpreter interpreter;

    @Transactional
    @RequestMapping("/receive-message")
    public void receiveMessage(Message message) {
        long groupId = message.getGroupId();
        Optional<String> response = interpreter.respond(message);
        response.ifPresent(content -> {
            long botId = getBot(groupId).getBotId();
            groupMe.sendMessage(content, botId);
        });
        interpreter.tryToWarnAll();
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
