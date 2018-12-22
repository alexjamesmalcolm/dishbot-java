package com.alexjamesmalcolm.dishbot;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity(name = "groupme")
public class Group {

    @Id
    private long id;

    @ManyToMany(cascade = ALL)
    private List<User> users = new ArrayList<>();

    private Group() {}

    public Group(long group_id) {
        id = group_id;
    }

    public List<User> getUsers() {
        return users;
    }

    public long getId() {
        return id;
    }

    public void addUser(User user) {
        users.add(user);
    }
}