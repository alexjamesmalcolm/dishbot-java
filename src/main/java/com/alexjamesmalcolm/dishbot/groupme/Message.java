package com.alexjamesmalcolm.dishbot.groupme;

import com.alexjamesmalcolm.dishbot.groupme.attachment.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toList;

public class Message {

    private List<Attachment> attachments;
    private URI avatarUrl;
    private Instant createdAt;
    private List<Long> favoritedBy;
    private Long groupId;
    private Long id;
    private String name;
    private Long senderId;
    private String senderType;
    private String sourceGuid;
    private Boolean system;
    private String text;
    private Long userId;
    private String platform;

    private Message() {}

    @JsonCreator
    private Message(
            @JsonProperty("attachments") List<Map> attachments,
            @JsonProperty("avatar_url") String avatarUrl,
            @JsonProperty("created_at") Integer createdAt,
            @JsonProperty("favorited_by") List<String> favoritedBy,
            @JsonProperty("group_id") String groupId,
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("sender_id") String senderId,
            @JsonProperty("sender_type") String senderType,
            @JsonProperty("source_guid") String sourceGuid,
            @JsonProperty("system") Boolean system,
            @JsonProperty("text") String text,
            @JsonProperty("user_id") String userId,
            @JsonProperty("platform") String platform
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(attachments);
        this.attachments = attachments.stream().map(attachment -> {
            String type = (String) attachment.get("type");
            switch (type) {
                case "image":
                    return objectMapper.convertValue(attachment, Image.class);
                case "location":
                    return objectMapper.convertValue(attachment, Location.class);
                case "split":
                    return objectMapper.convertValue(attachment, Split.class);
                case "emoji":
                    return objectMapper.convertValue(attachment, Emoji.class);
            }
            return null;
        }).collect(toList());
        this.avatarUrl = parseToUri(avatarUrl);
        this.createdAt = Instant.ofEpochMilli(createdAt);
        this.favoritedBy = favoritedBy.stream().map(Long::parseLong).collect(toList());
        this.groupId = parseLong(groupId);
        this.id = parseLong(id);
        this.name = name;
        this.senderId = parseLong(senderId);
        this.senderType = senderType;
        this.sourceGuid = sourceGuid;
        this.system = system;
        this.text = text;
        this.userId = parseLong(userId);
        this.platform = platform;
    }

    private URI parseToUri(String uri) {
        return uri != null && !uri.isEmpty() ? URI.create(uri) : null;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getText() {
        return text;
    }

    public Long getUserId() {
        return userId;
    }
}
