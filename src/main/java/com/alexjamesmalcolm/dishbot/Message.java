package com.alexjamesmalcolm.dishbot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Map;

@Entity
public class Message {

    @Id
    @GeneratedValue
    private long id;

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
