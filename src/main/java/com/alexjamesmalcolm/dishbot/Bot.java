package com.alexjamesmalcolm.dishbot;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Bot {
    // {name=Snackatron, bot_id=e3af5b0fbe6a6be70d16dbd9fc, group_id=37820787, group_name=Guinness VI Squad, avatar_url=null, callback_url=, dm_notification=false}

    @Id
    private String id;

    @OneToOne
    private Group group;
    private String name;

    private Bot() {}

    public Bot(String id, String name, Group group) {
        this.id = id;
        this.name = name;
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }
}
