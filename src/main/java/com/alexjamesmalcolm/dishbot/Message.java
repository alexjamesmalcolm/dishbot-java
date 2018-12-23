package com.alexjamesmalcolm.dishbot;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toMap;
import static javax.persistence.CascadeType.ALL;

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
    @ManyToOne(cascade = ALL)
    private User user;
    private String source_guid;
//    private URL avatar_url;
    @Lob
    private String text;
    private Timestamp created_at;
    @ManyToOne(cascade = ALL)
    private Group group;

    private Message() {
    }

    public Message (HttpServletRequest request) throws IOException, SystemMessageException {
        this(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
    }

    public Message(String json) throws MalformedURLException, SystemMessageException {
        System.out.println(json);
        // {"attachments":[],"avatar_url":"https://i.groupme.com/750x750.jpeg.83f02dee51d24c9386bce40c4da6d445","created_at":1545438872,"group_id":"46707218","id":"154543887253121474","name":"Alex Malcolm","sender_id":"19742906","sender_type":"user","source_guid":"b59709300225e65ebbecfb27ad36eb2a","system":false,"text":"test","user_id":"19742906"}
        String withTheEndsCutOff = json.substring(1, json.length() - 1);
        System.out.println(withTheEndsCutOff);
        Stream<String> stream = Arrays.stream(withTheEndsCutOff.split(","));
        Map<String, String> map = stream.collect(toMap(x -> {
            String key = x.split("\":")[0];
            key = key.replaceAll("\"", "");
            return key;
        }, x -> {
            String value = x.split("\":")[1];
            String key = x.split("\":")[0];
            key = key.replaceAll("\"", "");
            if (key.equals("text")) {
                value = value.substring(1, value.length() - 1);
            } else if (key.equals("attachments")) {
                value = value.substring(1, value.length() - 1);
            } else {
                value = value.replaceAll("\"", "");
            }
            return value;
        }));
        System.out.println(map);
        boolean system = parseBoolean(map.get("system"));
        if (system) {
            throw new SystemMessageException();
        }
        id = parseLong(map.get("id"));
        source_guid = map.get("source_guid");
        text = map.get("text");
        created_at = new Timestamp(1000 * parseLong(map.get("created_at")));
        long group_id = parseLong(map.get("group_id"));
        String name = map.get("name");
        long user_id = parseLong(map.get("user_id"));
        URL avatar_url = new URL(map.get("avatar_url"));
        user = new User(name, user_id, avatar_url);
        group = new Group(group_id);
        group.addUser(user);
    }

    public String getText() {
        return text;
    }

    public String getSourceGuid() {
        return source_guid;
    }

    public Timestamp getCreatedAt() {
        return created_at;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Group getGroup() {
        return group;
    }
}
