package com.alexjamesmalcolm.dishbot.groupme;

import static java.lang.Long.parseLong;

public class Bot {

    private String name;
    private String bot_id;
    private String group_id;
    private String group_name;
    private String avatar_url;
    private String callback_url;
    private String dm_notification;

    private Bot() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBot_id() {
        return bot_id;
    }

    public void setBot_id(String bot_id) {
        this.bot_id = bot_id;
    }

    public Long getGroup_id() {
        return parseLong(group_id);
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getCallback_url() {
        return callback_url;
    }

    public void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }

    public String getDm_notification() {
        return dm_notification;
    }

    public void setDm_notification(String dm_notification) {
        this.dm_notification = dm_notification;
    }
}
