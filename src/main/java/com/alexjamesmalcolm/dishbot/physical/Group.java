package com.alexjamesmalcolm.dishbot.physical;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity(name = "groupme")
public class Group {

    @Id
    private long id;

    @ManyToMany(cascade = ALL)
    private List<User> users = new ArrayList<>();
    @OneToOne(mappedBy = "group", cascade = ALL)
    private Bot bot;
    @OneToOne(cascade = ALL)
    private Wheel wheel;

    private Group() {}

    public Group(long group_id) {
        id = group_id;
        wheel = new Wheel(1234);
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

    public Bot getBot() {
        return bot;
    }

    public Wheel getWheel() {
        return wheel;
    }
}