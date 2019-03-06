package com.alexjamesmalcolm.dishbot.groupme;

import java.net.URI;
import java.util.List;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Long.parseLong;

public class Member {

    private String user_id;
    private String nickname;
    private String image_url;
    private String id;
    private String muted;
    private String autokicked;
    private List roles;
    private String name;

    private Member() {
    }

    public Long getUserId() {
        return parseLong(user_id);
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public URI getImageUrl() {
        return URI.create(image_url);
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Long getId() {
        return parseLong(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getMuted() {
        return parseBoolean(muted);
    }

    public void setMuted(String muted) {
        this.muted = muted;
    }

    public Boolean getAutokicked() {
        return parseBoolean(autokicked);
    }

    public void setAutokicked(String autokicked) {
        this.autokicked = autokicked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List roles) {
        this.roles = roles;
    }
}
