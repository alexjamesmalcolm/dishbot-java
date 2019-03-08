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

    private void setName(String name) {
        this.name = name;
    }

    public long getBotId() {
        return parseLong(bot_id);
    }

    private void setBot_id(String bot_id) {
        this.bot_id = bot_id;
    }

    public Long getGroup_id() {
        return parseLong(group_id);
    }

    private void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroupName() {
        return group_name;
    }

    private void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getAvatarUrl() {
        return avatar_url;
    }

    private void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getCallbackUrl() {
        return callback_url;
    }

    private void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }

    public String getDmNotification() {
        return dm_notification;
    }

    private void setDm_notification(String dm_notification) {
        this.dm_notification = dm_notification;
    }
}
