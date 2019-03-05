package com.alexjamesmalcolm.dishbot.bean;

import com.alexjamesmalcolm.dishbot.Properties;
import com.alexjamesmalcolm.dishbot.logical.BotMessage;
import com.alexjamesmalcolm.dishbot.physical.Bot;
import com.alexjamesmalcolm.dishbot.physical.Group;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
public class GroupMeService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private Properties properties;

    private String accessToken = properties.getAccessToken();

    public Bot getBot(Group group) {
        Bot bot = group.getBot();
        if (bot == null) {
            long groupId = group.getId();
            String str = properties.getBaseUrl() + "/bots?token=" + accessToken;
            URI uri = URI.create(str);
            Map<String, Object> results = restTemplate.getForObject(uri, Map.class);
            System.out.println(results);
            List<Map> bots = (List<Map>) results.get("response");
            // [{name=Snackatron, bot_id=e3af5b0fbe6a6be70d16dbd9fc, group_id=37820787, group_name=Guinness VI Squad, avatar_url=null, callback_url=, dm_notification=false}, {name=Test bot, bot_id=6dc7db8c212c036ddc9dc9acbc, group_id=46707218, group_name=Empty Bot Test Group, avatar_url=null, callback_url=https://dishbot.herokuapp.com/receive-message, dm_notification=false}]
            System.out.println(bots);
            bot = bots.stream().map(botMap -> {
                String bot_id = (String) botMap.get("bot_id");
                String name = (String) botMap.get("name");
                long group_id = Long.parseLong((String) botMap.get("group_id"));
                Group tempGroup = new Group(group_id);
                return new Bot(bot_id, name, tempGroup);
            }).filter(b -> b.getGroup().getId() == groupId).findFirst().get();
        }
        return bot;
    }

    public Group getGroupFromGroupMe(Long groupId) {
        // TODO Write a Rest method to get the group name and other data
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
}
