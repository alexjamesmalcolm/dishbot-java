package com.alexjamesmalcolm.dishbot;

import java.util.Map;

public class Message {

    private String senderName;
    private String content;
    private String attachments;

    public Message(Map<String, String> map) {
        senderName = map.get("name");
        content = map.get("text");
        attachments = map.get("attachments");
    }

    public String getSenderName() {
        return senderName;
    }

    public String getContent() {
        return content;
    }
}
