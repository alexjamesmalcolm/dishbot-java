package com.alexjamesmalcolm.dishbot.groupme;

import java.net.URI;
import java.util.List;

import static java.lang.Long.parseLong;

public class Message {

    private List attachments;
    private String avatar_url;
    private String created_at;
    private List favorited_by;
    private String group_id;
    private String id;
    private String name;
    private String sender_id;
    private String sender_type;
    private String source_guid;
    private String system;
    private String text;
    private String user_id;
    private String platform;

    private Message() {
    }

    public URI getAvatar_url() {
        return URI.create(avatar_url);
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Long getGroup_id() {
        return parseLong(group_id);
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_type() {
        return sender_type;
    }

    public void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public String getSource_guid() {
        return source_guid;
    }

    public void setSource_guid(String source_guid) {
        this.source_guid = source_guid;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public List getAttachments() {
        return attachments;
    }

    public void setAttachments(List attachments) {
        this.attachments = attachments;
    }

    public List getFavorited_by() {
        return favorited_by;
    }

    public void setFavorited_by(List favorited_by) {
        this.favorited_by = favorited_by;
    }
}
