package com.alexjamesmalcolm.dishbot.groupme;

import java.io.Serializable;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;

public class Group implements Serializable {

    private String id;
    private String group_id;
    private String name;
    private String phone_number;
    private String type;
    private String description;
    private String image_url;
    private String creator_user_id;
    private String created_at;
    private String office_mode;
    private String share_url;
    private String share_qr_code_url;
    private List<Member> members;
    private Map messages;
    private String max_members;
    private String updated_at;

    private Group() {
    }

    public Long getId() {
        return parseLong(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getGroup_id() {
        return parseLong(group_id);
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URI getImage_url() {
        return URI.create(image_url);
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Long getCreator_user_id() {
        return parseLong(creator_user_id);
    }

    public void setCreator_user_id(String creator_user_id) {
        this.creator_user_id = creator_user_id;
    }

    public Instant getCreated_at() {
        return Instant.ofEpochMilli(parseLong(created_at));
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Boolean getOffice_mode() {
        return Boolean.parseBoolean(office_mode);
    }

    public void setOffice_mode(String office_mode) {
        this.office_mode = office_mode;
    }

    public URI getShare_url() {
        return URI.create(share_url);
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public URI getShare_qr_code_url() {
        return URI.create(share_qr_code_url);
    }

    public void setShare_qr_code_url(String share_qr_code_url) {
        this.share_qr_code_url = share_qr_code_url;
    }

    public Integer getMax_members() {
        return Integer.parseInt(max_members);
    }

    public void setMax_members(String max_members) {
        this.max_members = max_members;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List members) {
        this.members = members;
    }

    public Map getMessages() {
        return messages;
    }

    public void setMessages(Map messages) {
        this.messages = messages;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
