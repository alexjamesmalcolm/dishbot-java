package com.alexjamesmalcolm.dishbot;

import org.springframework.web.client.RestTemplate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity(name = "groupme")
public class Group {

    @Id
    private long id;

    @ManyToMany(cascade = ALL)
    private List<User> users = new ArrayList<>();
    private String botId;

    private Group() {}

    public Group(long group_id) throws URISyntaxException {
        id = group_id;
        List foo = new RestTemplate().getForObject(new URI("/bots"), List.class);
        System.out.println(foo);
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

    public String getBotId() {
        return botId;
    }
}
