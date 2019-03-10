package com.alexjamesmalcolm.dishbot.groupme;

import com.alexjamesmalcolm.dishbot.groupme.attachment.Attachment;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toList;

public class Message {

    private List attachments;
    private String avatar_url;
    private Integer created_at;
    private List<String> favorited_by;
    private String group_id;
    private String id;
    private String name;
    private String sender_id;
    private String sender_type;
    private String source_guid;
    private Boolean system;
    private String text;
    private String user_id;
    private String platform;

    private Message() {
    }

    public URI getAvatarUrl() {
        return avatar_url != null && !avatar_url.isEmpty() ? URI.create(avatar_url) : null;
    }

    private void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public Instant getCreatedAt() {
        return Instant.ofEpochMilli(created_at);
    }

    private void setCreated_at(Integer created_at) {
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

    public String getSenderId() {
        return sender_id;
    }

    private void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSenderType() {
        return sender_type;
    }

    private void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public String getSourceGuid() {
        return source_guid;
    }

    private void setSource_guid(String source_guid) {
        this.source_guid = source_guid;
    }

    public Boolean getSystem() {
        return system;
    }

    private void setSystem(Boolean system) {
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

    public List<Attachment> getAttachments() {
        return attachments;
    }

    private void setAttachments(List attachments) {
        this.attachments = attachments;
    }

    public List<Long> getFavoritedBy() {
        return favorited_by.stream().map(Long::parseLong).collect(toList());
    }

    private void setFavorited_by(List favorited_by) {
        this.favorited_by = favorited_by;
    }
}
