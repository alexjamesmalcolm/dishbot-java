package com.alexjamesmalcolm.dishbot;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toMap;

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
    private long id;
    private String name;
    private String source_guid;
    private String sender_type;
    private String[] attachments;
    private URL avatar_url;
    @Lob
    private String text;
    private boolean system;
    private Timestamp created_at;
    private long group_id;
    private long sender_id;
    private long user_id;

    private Message() {
    }

    public Message (HttpServletRequest request) throws IOException {
        String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        String withTheEndsCutOff = json.substring(1, json.length() - 1).replace("\"", "");
        Stream<String> stream = Arrays.stream(withTheEndsCutOff.split(","));
        Map<String, String> map = stream.collect(toMap(x -> x.split(":")[0], x -> x.split(":")[1]));
        id = parseLong(map.get("id"));
        name = map.get("name");
        source_guid = map.get("source_guid");
        sender_type = map.get("sender_type");
        attachments = map.get("attachments").split(",");
        avatar_url = new URL(map.get("avatar_url"));
        text = map.get("text");
        system = parseBoolean(map.get("system"));
        created_at = new Timestamp(parseLong(map.get("created_at")));
        group_id = parseLong(map.get("group_id"));
        sender_id = parseLong(map.get("sender_id"));
        user_id = parseLong(map.get("user_id"));
    }

//    public Message(String[] attachments, String avatar_url, Long created_at, String group_id,
//                   String id, String name, String sender_id, String sender_type,
//                   String source_guid, Boolean system, String text, String user_id) {
//        System.out.println(attachments);
//        System.out.println(avatar_url);
//        System.out.println(created_at);
//        System.out.println(group_id);
//        System.out.println(id);
//        System.out.println(name);
//        System.out.println(sender_id);
//        System.out.println(sender_type);
//        System.out.println(source_guid);
//        System.out.println(system);
//        System.out.println(text);
//        System.out.println(user_id);
//        this.id = Long.parseLong(id);
//        this.name = name;
//        this.text = text;
//        this.attachments = attachments;
//        try {
//            this.avatar_url = new URL(avatar_url);
//        } catch (MalformedURLException e) {
//            e.fillInStackTrace();
//        }
//        this.sender_type = sender_type;
//        this.source_guid = source_guid;
//        this.system = system;
//        this.created_at = new Timestamp(created_at);
//        this.group_id = Long.parseLong(group_id);
//        this.sender_id = Long.parseLong(sender_id);
//        this.user_id = Long.parseLong(user_id);
//    }

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
