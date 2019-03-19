package com.alexjamesmalcolm.dishbot.physical;

import com.alexjamesmalcolm.groupme.response.Group;
import com.alexjamesmalcolm.groupme.response.Me;
import com.alexjamesmalcolm.groupme.service.GroupMeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.net.URI;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Component
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

    @Transient
    private static GroupMeService groupMe;
    @Transient
    private List<Group> groups;
    @Transient
    private Me me;

    private Account() {}

    public Account(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    @Autowired
    public void setGroupMeService(GroupMeService groupMeService) {
        Account.groupMe = groupMeService;
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

    public boolean isInGroup(Long groupId) {
        return getGroupIds().contains(groupId);
    }

    public void updateToken(String token) {
        this.token = token;
    }

    public void updateAccount(String token) {
        updateToken(token);
    }

    public Collection<Group> getGroups() {
        if (groups == null) {
            groups = groupMe.getAllGroups(token);
            groupIds = groups.stream().map(Group::getGroupId).collect(toList());
        }
        return groups;
    }

    private void initiateMe() {
        if (me == null) {
            me = groupMe.getMe(token);
        }
    }

    public String getName() {
        initiateMe();
        return me.getName();
    }

    public URI getImage() {
        initiateMe();
        return me.getImageUrl();
    }
}
