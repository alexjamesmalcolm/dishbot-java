package com.alexjamesmalcolm.dishbot.physical;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Wheel {

    @Id
    @GeneratedValue
    private long id;

    public long getId() {
        return id;
    }
}
