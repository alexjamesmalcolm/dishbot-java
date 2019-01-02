package com.alexjamesmalcolm.dishbot;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Wheel {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @OneToOne
    private Group group;
    private Duration durationUntilFine;
    @OneToMany
    private List<User> roommates;
    private long fineAmount;
    private int index;
    private Timestamp timeShiftStarted;


    private Wheel() {
    }

    public Wheel(long id) {
        this.id = id;
        roommates = new ArrayList<>();
        index = 0;
    }

    public User getRoommateAfter(User roommate) {
        int i = roommates.indexOf(roommate);
        try {
            return roommates.get(i + 1);
        } catch (IndexOutOfBoundsException e) {
            return roommates.get(0);
        }
    }

    public User getCurrentRoommate() {
        return roommates.get(index);
    }

    public Duration getDurationUntilFine() {
        return durationUntilFine;
    }

    public void setDurationUntilFine(Duration durationUntilFine) {
        this.durationUntilFine = durationUntilFine;
    }

    public Duration getDurationUntilFineForCurrentRoommate() {
        Instant instantShiftStarted = timeShiftStarted.toInstant();
        return durationUntilFine.minus(Duration.between(instantShiftStarted, now()));
    }

    public void addRoommate(User roommate) {
        roommates.add(roommate);
        if (timeShiftStarted == null) {
            timeShiftStarted = Timestamp.from(now());
        }
    }

    public List<User> getRoommatesInOrder() {
        return roommates;
    }

    public void setFineAmount(long fine) {
        fineAmount = fine;
    }

    public long getFineAmount() {
        return fineAmount;
    }

    public void advanceWheel() {
        index += 1;
        try {
            roommates.get(index);
        } catch(IndexOutOfBoundsException e) {
            index = 0;
        }
        timeShiftStarted = Timestamp.from(now());
    }
}
