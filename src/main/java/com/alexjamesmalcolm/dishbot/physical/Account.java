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
    private String accessToken;
    private Long userId;
    @ElementCollection
    private Collection<Long> groupIds;
    @OneToMany(mappedBy = "owner")
    private Collection<Wheel> wheels;

    public Account(String accessToken, Long userId, Collection<Wheel> wheels, Collection<Group> groups) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.wheels = wheels;
        updateGroups(groups);
    }

    public String getAccessToken() {
        return accessToken;
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

    public void updateGroups(Collection<Group> groups) {
        this.groupIds = groups.stream().map(Group::getGroupId).collect(toList());
    }

    public boolean isInGroup(Long groupId) {
        return groupIds.contains(groupId);
    }
}
