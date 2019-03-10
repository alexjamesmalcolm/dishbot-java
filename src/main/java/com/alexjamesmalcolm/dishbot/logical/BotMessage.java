package com.alexjamesmalcolm.dishbot.logical;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BotMessage {
    private final String text;
    private final String botId;

    public BotMessage(String text, String botId) {
        this.text = text;
        this.botId = botId;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("bot_id")
    public String getBotId() {
        return botId;
    }
}
