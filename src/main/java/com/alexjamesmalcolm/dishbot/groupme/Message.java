package com.alexjamesmalcolm.dishbot.groupme;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.net.URI;
import java.util.List;

import static java.lang.Long.parseLong;

public class Message extends Response {

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

//    @JsonCreator
    private Message(
            List attachments,
            String avatarUrl,
            String createdAt,
            List favoritedBy,
            String groupId,
            String id,
            String name,
            String senderId,
            String senderType,
            String sourceGuid,
            String system,
            String text,
            String userId,
            String platform
    ) {}

    private Message() {
    }

    public URI getAvatar_url() {
        return URI.create(avatar_url);
    }

    private void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    private void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getGroupId() {
        return parseLong(group_id);
    }

    private void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getSender_id() {
        return sender_id;
    }

    private void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_type() {
        return sender_type;
    }

    private void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public String getSource_guid() {
        return source_guid;
    }

    private void setSource_guid(String source_guid) {
        this.source_guid = source_guid;
    }

    public String getSystem() {
        return system;
    }

    private void setSystem(String system) {
        this.system = system;
    }

    public String getText() {
        return text;
    }

    private void setText(String text) {
        this.text = text;
    }

    public long getUserId() {
        return parseLong(user_id);
    }

    private void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPlatform() {
        return platform;
    }

    private void setPlatform(String platform) {
        this.platform = platform;
    }

    public List getAttachments() {
        return attachments;
    }

    private void setAttachments(List attachments) {
        this.attachments = attachments;
    }

    public List getFavorited_by() {
        return favorited_by;
    }

    private void setFavorited_by(List favorited_by) {
        this.favorited_by = favorited_by;
    }
}
