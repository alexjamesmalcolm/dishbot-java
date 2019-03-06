package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.bean.GroupMeService;
import com.alexjamesmalcolm.dishbot.bean.Interpreter;
import com.alexjamesmalcolm.dishbot.groupme.Group;
import com.alexjamesmalcolm.dishbot.groupme.Message;
import com.alexjamesmalcolm.dishbot.logical.BotMessage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
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
        BotMessage response = interpreter.respond(message);
        groupMe.sendMessage(response);
    }

    @GetMapping("/message")
    public Iterable<Message> getMessages() {
        List<Group> groups = groupMe.getAllGroups();
        return groups.stream().map(group -> {
            long groupId = group.getGroup_id();
            return groupMe.getMessages(groupId);
        }).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
