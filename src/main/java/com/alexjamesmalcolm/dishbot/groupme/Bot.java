package com.alexjamesmalcolm.dishbot.groupme;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Long.parseLong;

public class Bot {

    private String name;
    private String botId;
    private Long groupId;
    private String groupName;
    private URI avatarUrl;
    private URI callbackUrl;
    private Boolean dmNotification;

    @JsonCreator
    private Bot(
            @JsonProperty("name") String name,
            @JsonProperty("bot_id") String botId,
            @JsonProperty("group_id") String groupId,
            @JsonProperty("group_name") String groupName,
            @JsonProperty("avatar_url") String avatarUrl,
            @JsonProperty("callback_url") String callbackUrl,
            @JsonProperty("dm_notification") String dmNotification
    ) {
        this.name = name;
        this.botId = botId;
        this.groupId = parseLong(groupId);
        this.groupName = groupName;
        this.avatarUrl = parseToUri(avatarUrl);
        this.callbackUrl = parseToUri(callbackUrl);
        this.dmNotification = parseBoolean(dmNotification);
    }

    private URI parseToUri(String uri) {
        return uri != null ? URI.create(uri) : null;
    }

    public String getBotId() {
        return botId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public URI getCallbackUrl() {
        return callbackUrl;
    }
}
