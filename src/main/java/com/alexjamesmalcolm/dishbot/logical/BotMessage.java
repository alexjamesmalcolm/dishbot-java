package com.alexjamesmalcolm.dishbot.logical;

public class BotMessage {
    private final String text;
    private final String bot_id;

    public BotMessage(String text, String bot_id) {
        this.text = text;
        this.bot_id = bot_id;
    }

    public String getText() {
        return text;
    }

    public String getBot_id() {
        return bot_id;
    }
}
