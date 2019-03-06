package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.bean.GroupMeService;
import com.alexjamesmalcolm.dishbot.bean.Interpreter;
import com.alexjamesmalcolm.dishbot.groupme.Message;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

@RestController
public class MessageController {

    @Resource
    private EntityManager em;

    @Resource
    private GroupMeService groupMe;

    @Resource
    private Interpreter interpreter;

    @Transactional
    @RequestMapping("/receive-message")
    public void receiveMessage(Message message) {
        System.out.println(message);
    }

    @GetMapping("/message")
    public Iterable<Message> getMessages() {
        return null;
    }
}
