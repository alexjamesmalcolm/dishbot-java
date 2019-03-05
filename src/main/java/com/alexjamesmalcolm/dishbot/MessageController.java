package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.bean.GroupMeService;
import com.alexjamesmalcolm.dishbot.bean.Interpreter;
import com.alexjamesmalcolm.dishbot.exception.BotMessageException;
import com.alexjamesmalcolm.dishbot.exception.SystemMessageException;
import com.alexjamesmalcolm.dishbot.logical.BotMessage;
import com.alexjamesmalcolm.dishbot.physical.Bot;
import com.alexjamesmalcolm.dishbot.physical.Message;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class MessageController {

    @Resource
    private MessageRepository messageRepo;

    @Resource
    private EntityManager em;

    @Resource
    private BotRepository botRepo;

    @Resource
    private Interpreter interpreter;

    @Resource
    private GroupMeService groupMe;

    @Transactional
    @RequestMapping("/receive-message")
    public void receiveMessage(HttpServletRequest request) {
        try {
            Message message = new Message(request);
            long id = messageRepo.save(message).getId();
            em.flush();
            em.clear();
            message = messageRepo.findById(id).get();
            Bot bot = groupMe.getBot(message.getGroup());
            botRepo.save(bot);
            em.flush();
            em.clear();
            message = messageRepo.findById(id).get();
            Optional<BotMessage> response = interpreter.respond(message);
            response.ifPresent((BotMessage botMessage) -> groupMe.sendMessage(botMessage));
        } catch (BotMessageException e) {
            System.out.println("Message was from Bot");
        } catch (SystemMessageException e) {
            System.out.println("Message was from System");
        }
    }

    @GetMapping("/message")
    public Iterable<Message> getMessages() {
        return messageRepo.findAll();
    }
}
