package com.alexjamesmalcolm.dishbot.groupme;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;

public class Me {

    private Instant createdAt;
    private String email;
    private Boolean facebookConnected;
    private Long id;
    private URI imageUrl;
    private String locale;
    private String name;
    private String phoneNumber;
    private Boolean sms;
    private Boolean twitterConnected;
    private Instant updatedAt;
    private Long userId;
    private String zipCode;
    private URI shareUrl;
    private URI shareQrCodeUrl;
    private Map mfa;
    private List<String> tags;

    @JsonCreator
    private Me(
            @JsonProperty("created_at") Integer createdAt,
            @JsonProperty("email") String email,
            @JsonProperty("facebook_connected") Boolean facebookConnected,
            @JsonProperty("id") String id,
            @JsonProperty("image_url") String imageUrl,
            @JsonProperty("locale") String locale,
            @JsonProperty("name") String name,
            @JsonProperty("phone_number") String phoneNumber,
            @JsonProperty("sms") Boolean sms,
            @JsonProperty("twitter_connected") Boolean twitterConnected,
            @JsonProperty("updated_at") Integer updatedAt,
            @JsonProperty("user_id") String userId,
            @JsonProperty("zip_code") String zipCode,
            @JsonProperty("share_url") String shareUrl,
            @JsonProperty("share_qr_code_url") String shareQrCodeUrl,
            @JsonProperty("mfa") Map mfa,
            @JsonProperty("tags") List tags
    ) {
        this.createdAt = Instant.ofEpochMilli(createdAt);
        this.email = email;
        this.facebookConnected = facebookConnected;
        this.id = parseLong(id);
        this.imageUrl = parseToUrl(imageUrl);
        this.locale = locale;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.sms = sms;
        this.twitterConnected = twitterConnected;
        this.updatedAt = Instant.ofEpochMilli(updatedAt);
        this.userId = parseLong(userId);
        this.zipCode = zipCode;
        this.shareUrl = parseToUrl(shareUrl);
        this.shareQrCodeUrl = parseToUrl(shareQrCodeUrl);
        this.mfa = mfa;
        this.tags = tags;
    }

    private URI parseToUrl(String uri) {
        return uri != null && !uri.isEmpty() ? URI.create(uri) : null;
    }

    public Long getUserId() {
        return userId;
    }
}
