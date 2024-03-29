package com.alexjamesmalcolm.dishbot;

import javax.persistence.*;
import java.net.URL;
import java.util.Collection;

import static javax.persistence.CascadeType.ALL;

@Entity(name = "dishwasher")
public class User {

    @Id
    private long id;
    private String name;
    @OneToMany(mappedBy = "user")
    private Collection<Message> messages;
    private URL avatar_url;

    @ManyToMany(mappedBy = "users", cascade = ALL)
    private Collection<Group> groups;

    private User() {}

    public User(String name, long user_id, URL avatar_url) {
        this.name = name;
        id = user_id;
        this.avatar_url = avatar_url;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public long getId() {
        return id;
    }

    public Collection<Group> getGroups() {
        return groups;
    }
}
