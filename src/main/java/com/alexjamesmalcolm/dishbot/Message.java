package com.alexjamesmalcolm.dishbot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Message {

//    {
//        "attachments": [],
//        "avatar_url": "https://i.groupme.com/123456789",
//            "created_at": 1302623328,
//            "group_id": "1234567890",
//            "id": "1234567890",
//            "name": "John",
//            "sender_id": "12345",
//            "sender_type": "user",
//            "source_guid": "GUID",
//            "system": false,
//            "text": "Hello world ☃☃",
//            "user_id": "1234567890"
//    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    private String name;
    private String source_guid;
    private String sender_type;
    private String[] attachments;
    private URL avatar_url;
    private String text;
    private boolean system;
    private Timestamp created_at;
    private long group_id;
    private long sender_id;
    private long user_id;

    private Message() {
    }

    public Message(String[] attachments, String avatar_url, long created_at, String group_id,
                   String id, String name, String sender_id, String sender_type,
                   String source_guid, boolean system, String text, String user_id) {
        
        this.id = Long.parseLong(id);
        this.name = name;
        this.text = text;
        this.attachments = attachments;
        try {
            this.avatar_url = new URL(avatar_url);
        } catch (MalformedURLException e) {
            e.fillInStackTrace();
        }
        this.sender_type = sender_type;
        this.source_guid = source_guid;
        this.system = system;
        this.created_at = new Timestamp(created_at);
        this.group_id = Long.parseLong(group_id);
        this.sender_id = Long.parseLong(sender_id);
        this.user_id = Long.parseLong(user_id);
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getSourceGuid() {
        return source_guid;
    }

    public long getGroupId() {
        return group_id;
    }

    public String getSenderType() {
        return sender_type;
    }

    public String[] getAttachments() {
        return attachments;
    }

    public URL getAvatarUrl() {
        return avatar_url;
    }

    public Timestamp getCreatedAt() {
        return created_at;
    }

    public long getSenderId() {
        return sender_id;
    }

    public long getUserId() {
        return user_id;
    }

    public boolean isSystem() {
        return system;
    }

    public long getId() {
        return id;
    }
}
