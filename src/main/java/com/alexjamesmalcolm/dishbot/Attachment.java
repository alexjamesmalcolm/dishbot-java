package com.alexjamesmalcolm.dishbot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Attachment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    private Attachment() {}

    public Attachment(String json) {
    }
}
