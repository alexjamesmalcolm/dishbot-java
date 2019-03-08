package com.alexjamesmalcolm.dishbot.physical;

import com.alexjamesmalcolm.dishbot.groupme.Member;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Wheel {

    @Id
    @GeneratedValue
    private long id;
    private long groupId;
    private long currentUserId;

    @ElementCollection
    private List<Long> userIds;
    private Duration fineDuration;
    private Instant currentStart;

    private Wheel() {
    }

    public Wheel(long groupId) {
        userIds = new ArrayList<>();
        this.groupId = groupId;
        this.fineDuration = Duration.ofHours(48);
    }

    public Wheel(long groupId, Duration fineDuration) {
        this(groupId);
        this.fineDuration = fineDuration;
    }

    public long getId() {
        return id;
    }

    public void advanceWheel() {
        int index = userIds.indexOf(currentUserId);
        if (userIds.size() == index + 1) {
            currentUserId = userIds.get(0);
        } else {
            currentUserId = userIds.get(index + 1);
        }
    }

    public void addMember(Member member) {
        addMember(member.getUserId());
    }

    public void addMember(long userId) {
        userIds.add(userId);
        if (currentUserId == 0) {
            currentUserId = userId;
        }
        currentStart = Instant.now();
    }

    public long getCurrentMemberUserId() {
        return currentUserId;
    }

    public Duration getFineDuration() {
        return fineDuration;
    }

    public void setFineDuration(Duration fineDuration) {
        this.fineDuration = fineDuration;
    }

    public Duration getDurationUntilFineForCurrent() {
        if (currentUserId == 0) {
            return null;
        } else {
            Duration timeElapsed = Duration.between(currentStart, Instant.now());
            return fineDuration.minus(timeElapsed);
        }
    }
}
