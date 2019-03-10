package com.alexjamesmalcolm.dishbot.groupme;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Long.parseLong;

public class Me extends Response {

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

    private void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setFacebook_connected(String facebook_connected) {
        this.facebook_connected = facebook_connected;
    }

    private void setId(String id) {
        this.id = id;
    }

    private void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    private void setLocale(String locale) {
        this.locale = locale;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    private void setSms(String sms) {
        this.sms = sms;
    }

    private void setTwitter_connected(String twitter_connected) {
        this.twitter_connected = twitter_connected;
    }

    private void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    private void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    private void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    private void setShare_qr_code_url(String share_qr_code_url) {
        this.share_qr_code_url = share_qr_code_url;
    }

    private void setMfa(Map mfa) {
        this.mfa = mfa;
    }

    private void setTags(List tags) {
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
