package com.alexjamesmalcolm.dishbot.controller;

import com.alexjamesmalcolm.dishbot.AccountRepository;
import com.alexjamesmalcolm.dishbot.Properties;
import com.alexjamesmalcolm.dishbot.bean.Composer;
import com.alexjamesmalcolm.dishbot.physical.Account;
import com.alexjamesmalcolm.groupme.response.Message;
import com.alexjamesmalcolm.groupme.service.GroupMeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rollbar.notifier.Rollbar;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class GroupMeController {

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
    @PostMapping("/account/{accountId}/message")
    public void groupMePostCallback(HttpServletRequest request, @PathVariable Long accountId) throws IOException {
        String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Message message = mapper.readValue(json, Message.class);
        Map<String, Object> info = new HashMap<>();
        info.put("json", json);
        info.put("message", message);
        log.info("Received Message", info);
        long groupId = message.getGroupId();
        Optional<Account> optionalAccount = accountRepo.findById(accountId);
        Account account = optionalAccount.get();
        Optional<String> response = composer.respond(account, message);
        response.ifPresent(content -> {
            String botId = groupMe.getBot(account.getAccessToken(), groupId, properties.getDishbotUrl()).get().getBotId();
            groupMe.sendMessage(content, botId);
        });
        composer.tryToFineAll();
        composer.tryToWarnAll();
    }
}
