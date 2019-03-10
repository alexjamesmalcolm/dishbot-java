package com.alexjamesmalcolm.dishbot.groupme;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Long.parseLong;

public class Group {

    private Long id;
    private Long groupId;
    private String name;
    private String phoneNumber;
    private String type;
    private String description;
    private URI imageUrl;
    private Long creatorUserId;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean officeMode;
    private URI shareUrl;
    private URI shareQrCodeUrl;
    private List<Member> members;
    private Long maxMembers;
    private Integer messageCount;
    private Long lastMessageId;
    private Instant lastMessageCreatedAt;
    private String lastMessageSenderNickname;
    private String lastMessageText;
    private URI lastMessageSenderImageUrl;
    private List lastMessageAttachments;

    @JsonCreator
    private Group(
            @JsonProperty("id") String id,
            @JsonProperty("group_id") String groupId,
            @JsonProperty("name") String name,
            @JsonProperty("phone_number") String phoneNumber,
            @JsonProperty("type") String type,
            @JsonProperty("description") String description,
            @JsonProperty("image_url") String imageUrl,
            @JsonProperty("creator_user_id") String creatorUserId,
            @JsonProperty("created_at") String createdAt,
            @JsonProperty("updated_at") String updatedAt,
            @JsonProperty("office_mode") String officeMode,
            @JsonProperty("share_url") String shareUrl,
            @JsonProperty("share_qr_code_url") String shareQrCodeUrl,
            @JsonProperty("members") List members,
            @JsonProperty("messages") Map messages,
            @JsonProperty("max_members") String maxMembers
    ) {
        this.id = parseLong(id);
        this.groupId = parseLong(groupId);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.description = description;
        this.imageUrl = parseToUri(imageUrl);
        this.creatorUserId = parseLong(creatorUserId);
        this.createdAt = Instant.ofEpochMilli(parseLong(createdAt));
        this.updatedAt = Instant.ofEpochMilli(parseLong(updatedAt));
        this.officeMode = parseBoolean(officeMode);
        this.shareUrl = parseToUri(shareUrl);
        this.shareQrCodeUrl = parseToUri(shareQrCodeUrl);
        this.members = Arrays.asList(new ObjectMapper().convertValue(members, Member[].class));
        this.maxMembers = parseLong(maxMembers);
        this.messageCount = (Integer) messages.get("count");
        this.lastMessageId = parseLong((String) messages.get("last_message_id"));
        this.lastMessageCreatedAt = Instant.ofEpochMilli((Integer) messages.get("last_message_created_at"));
        Map preview = (Map) messages.get("preview");
        this.lastMessageSenderNickname = (String) preview.get("nickname");
        this.lastMessageText = (String) preview.get("text");
        this.lastMessageSenderImageUrl = parseToUri((String) preview.get("image_url"));
        this.lastMessageAttachments = (List) preview.get("attachments");
    }

    private URI parseToUri(String uri) {
        return uri != null ? URI.create(uri) : null;
    }

    public Long getGroupId() {
        return groupId;
    }

    public List<Member> getMembers() {
        return members;
    }

    public Optional<Member> queryForMember(Long userId) {
        return members.stream().filter(member -> member.getUserId().equals(userId)).findFirst();
    }

    public Long getId() {
        return id;
    }
}
