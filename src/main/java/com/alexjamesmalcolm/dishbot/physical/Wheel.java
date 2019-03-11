package com.alexjamesmalcolm.dishbot.physical;

import com.alexjamesmalcolm.dishbot.groupme.Member;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private boolean hasWarnedCurrent;
    private Double fineAmount;
    private Map<Long, Double> fines;

    private Wheel() {
    }

    public Wheel(long groupId) {
        this.userIds = new ArrayList<>();
        this.groupId = groupId;
        this.fineDuration = Duration.ofHours(48);
        this.hasWarnedCurrent = false;
        this.fineAmount = 5.0;
        this.fines = new HashMap<>();
    }

    public Wheel(long groupId, Duration fineDuration) {
        this(groupId);
        this.fineDuration = fineDuration;
    }

    public Wheel(long groupId, Double fineAmount) {
        this(groupId);
        this.fineAmount = fineAmount;
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
        hasWarnedCurrent = false;
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
        fines.put(userId, 0.0);
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

    public boolean hasWarnedCurrent() {
        return hasWarnedCurrent;
    }

    public boolean needToWarnCurrent() {
        if (!hasWarnedCurrent) {
            Duration timeLeft = getDurationUntilFineForCurrent();
            Duration halfOfTimeGiven = getFineDuration().dividedBy(2);
            return timeLeft.compareTo(halfOfTimeGiven) <= 0;
        }
        return false;
    }

    public void currentHasBeenWarned() {
        hasWarnedCurrent = true;
    }

    public long getGroupId() {
        return groupId;
    }

    public Double getFineAmount() {
        return fineAmount;
    }

    public Map<Long, Double> getFineAmounts() {
        return fines;
    }

    public boolean isExpired() {
        Duration timeLeft = getDurationUntilFineForCurrent();
        return timeLeft.isZero() || timeLeft.isNegative();
    }

    public void giveFine(Member member) {
        giveFine(member.getUserId());
    }

    public void giveFine(Long userId) {
        Double previousFine = fines.get(userId);
        fines.put(userId, previousFine + fineAmount);
    }

    public void removeMember(Long userId) {
        userIds.remove(userId);
    }
}
