package com.alexjamesmalcolm.dishbot.physical;

import com.alexjamesmalcolm.groupme.response.Group;

import javax.persistence.*;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Entity
public class Account {

    @Id
    @GeneratedValue
    private long id;
    private String token;
    private Long userId;
    @ElementCollection
    private Collection<Long> groupIds;
    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    private Collection<Wheel> wheels;

    private Account() {}

    public Account(String token, Long userId, Collection<Group> groups) {
        this.token = token;
        this.userId = userId;
        updateGroups(groups);
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public Collection<Long> getGroupIds() {
        return groupIds;
    }

    public Collection<Wheel> getWheels() {
        return wheels;
    }

    private void updateGroups(Collection<Group> groups) {
        this.groupIds = groups.stream().map(Group::getGroupId).collect(toList());
    }

    public boolean isInGroup(Long groupId) {
        return groupIds.contains(groupId);
    }

    public void updateToken(String token) {
        this.token = token;
    }

    public void updateAccount(String token, Collection<Group> groups) {
        updateToken(token);
        updateGroups(groups);
    }
}
