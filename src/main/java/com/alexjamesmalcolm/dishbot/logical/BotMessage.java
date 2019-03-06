package com.alexjamesmalcolm.dishbot.logical;

public class BotMessage {
    private final String text;
    private final String bot_id;

    public BotMessage(String text, Long bot_id) {
        this.text = text;
        this.bot_id = bot_id.toString();
    }

    public String getText() {
        return text;
    }

    public String getBotId() {
        return bot_id;
    }
}
