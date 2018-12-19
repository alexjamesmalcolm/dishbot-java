package com.alexjamesmalcolm.dishbot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Map;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    private String senderName;
    private String content;
    private String attachments;

    private Message() {}

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

    public long getId() {
        return id;
    }
}
