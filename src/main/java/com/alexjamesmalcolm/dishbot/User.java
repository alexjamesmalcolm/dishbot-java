package com.alexjamesmalcolm.dishbot;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "dishwasher")
public class User {

    @Id
    private long id;
    private String name;
    @OneToMany(mappedBy = "user")
    private Collection<Message> messages;

    @ManyToMany
    private Collection<Group> groups;

    private User() {}

    public User(String name, long user_id) {
        this.name = name;
        id = user_id;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public long getId() {
        return id;
    }
}
