package com.alexjamesmalcolm.dishbot.controller;

import com.alexjamesmalcolm.dishbot.AccountRepository;
import com.alexjamesmalcolm.dishbot.Properties;
import com.alexjamesmalcolm.dishbot.bean.Composer;
import com.alexjamesmalcolm.dishbot.physical.Account;
import com.alexjamesmalcolm.groupme.response.Group;
import com.alexjamesmalcolm.groupme.response.Message;
import com.alexjamesmalcolm.groupme.service.GroupMeService;
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

import static java.util.stream.Collectors.toList;

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

    @Resource
    private Properties properties;

    @Resource
    private AccountRepository accountRepo;

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
        Optional<Account> optionalAccount = accountRepo.findByUserId(message.getUserId());
        Account account = optionalAccount.orElseGet(() -> accountRepo.findByGroupId(groupId).get());
        Optional<String> response = composer.respond(account, message);
        response.ifPresent(content -> {
            String botId = groupMe.getBot(account.getAccessToken(), groupId, properties.getDishbotUrl()).get().getBotId();
            groupMe.sendMessage(content, botId);
        });
        composer.tryToFineAll();
        composer.tryToWarnAll();
    }

    @GetMapping("/message")
    public Iterable<Message> getMessages() {
        Collection<Account> accounts = accountRepo.findAll();
        return accounts.stream().map(Account::getAccessToken).map(accessToken -> {
            List<Group> groups = groupMe.getAllGroups(accessToken);
            return groups.stream().map(group -> {
                List<Message> messages = groupMe.getMessages(accessToken, group.getGroupId());
                return messages.stream().distinct().collect(toList());
            }).flatMap(Collection::stream).collect(toList());
        }).flatMap(Collection::stream).collect(toList());
    }
}
