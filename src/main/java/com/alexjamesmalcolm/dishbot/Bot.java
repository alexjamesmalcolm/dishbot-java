package com.alexjamesmalcolm.dishbot;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Bot {

    @Id
    private String id;

    @OneToOne
    private Group group;

    private Bot() {}

    public Bot(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
