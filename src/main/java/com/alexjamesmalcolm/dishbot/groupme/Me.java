package com.alexjamesmalcolm.dishbot.groupme;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Long.parseLong;

public class Me {

    private String created_at;
    private String email;
    private String facebook_connected;
    private String id;
    private String image_url;
    private String locale;
    private String name;
    private String phone_number;
    private String sms;
    private String twitter_connected;
    private String updated_at;
    private String user_id;
    private String zip_code;
    private String share_url;
    private String share_qr_code_url;
    private Map mfa;
    private List tags;

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFacebook_connected(String facebook_connected) {
        this.facebook_connected = facebook_connected;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public void setTwitter_connected(String twitter_connected) {
        this.twitter_connected = twitter_connected;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public void setShare_qr_code_url(String share_qr_code_url) {
        this.share_qr_code_url = share_qr_code_url;
    }

    public void setMfa(Map mfa) {
        this.mfa = mfa;
    }

    public void setTags(List tags) {
        this.tags = tags;
    }

    public Instant getCreated_at() {
        return Instant.ofEpochMilli(parseLong(created_at));
    }

    public String getEmail() {
        return email;
    }

    public boolean getFacebook_connected() {
        return parseBoolean(facebook_connected);
    }

    public long getId() {
        return parseLong(id);
    }

    public URI getImage_url() {
        return URI.create(image_url);
    }

    public String getLocale() {
        return locale;
    }

    public String getName() {
        return name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public boolean getSms() {
        return parseBoolean(sms);
    }

    public boolean getTwitter_connected() {
        return parseBoolean(twitter_connected);
    }

    public Instant getUpdated_at() {
        return Instant.ofEpochMilli(parseLong(updated_at));
    }

    public long getUser_id() {
        return parseLong(user_id);
    }

    public String getZip_code() {
        return zip_code;
    }

    public URI getShare_url() {
        return URI.create(share_url);
    }

    public URI getShare_qr_code_url() {
        return URI.create(share_qr_code_url);
    }

    public Map getMfa() {
        return mfa;
    }

    public List getTags() {
        return tags;
    }
}
