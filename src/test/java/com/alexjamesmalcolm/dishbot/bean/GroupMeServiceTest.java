package com.alexjamesmalcolm.dishbot.bean;

import com.alexjamesmalcolm.dishbot.Properties;
import com.alexjamesmalcolm.dishbot.groupme.*;
import com.alexjamesmalcolm.dishbot.logical.BotMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupMeServiceTest {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private Properties properties;

    @Resource
    private GroupMeService underTest;

    private long groupId;

    @Before
    public void setup() {
        assumeThat("Unable to connect to GroupMe.", properties.getEnvironment(), is(not("work")));
        groupId = 46707218;
    }

    @Test
    public void shouldNotBeNull() {
        assertNotNull(restTemplate);
        assertNotNull(underTest);
        assertNotNull(properties);
    }

    @Test
    public void shouldReturnGroupWithCorrectId() {
        Group group = underTest.getGroup(groupId);
        assertThat(group.getId(), is(groupId));
    }

    @Test
    public void shouldHaveOneMember() {
        Group group = underTest.getGroup(groupId);
        List<Member> members = group.getMembers();
        assertThat(members.size(), is(1));
    }

    @Test
    public void shouldGetTwentyMessagesByDefault() {
        List<Message> messages = underTest.getMessages(groupId);
        assertThat(messages.size(), is(20));
    }

    @Test
    public void shouldGetBotForGroup() {
        List<Bot> bots = underTest.getBots(groupId);
        Assert.assertThat(bots, is(not(empty())));
    }

    @Test
    public void shouldGetTenGroupsByDefault() {
        List<Group> groups = underTest.getAllGroups();
        assertThat(groups.size(), is(10));
    }

    @Test
    public void checkThatGroupMeIsAvailable() {
        Map json = restTemplate.getForObject(properties.getBaseUrl() + "/users/me?token=" + properties.getGroupMeAccessToken(), Map.class);
        ObjectMapper objectMapper = new ObjectMapper();
        Me me = objectMapper.convertValue(json.get("response"), Me.class);
        assertNotNull(me);
    }

    @Test
    public void shouldGetGroupFromWrappedEnvelope() {
        String path = properties.getBaseUrl() + "/groups/" + groupId + "?token=" + properties.getGroupMeAccessToken();
        Map rawEnvelope = restTemplate.getForObject(path, Map.class);
        ObjectMapper objectMapper = new ObjectMapper();
        Envelope envelope = objectMapper.convertValue(rawEnvelope, Envelope.class);
        Group group = envelope.getResponse(Group.class);
        assertNotNull(group);
    }

    @Test
    public void shouldGetMeWithMyUserId() {
        Long expected = 19742906L;
        Me me = underTest.getMe();
        Long actual = me.getUserId();
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldGetMyselfFromMyTestGroupAsAMember() {
        String expected = "Alex Malcolm";
        Group group = underTest.getGroup(groupId);
        List<Member> members = group.getMembers();
        Member member = members.get(0);
        assertThat(member.getName(), is(expected));
    }

    @Ignore("Skipping to avoid creating messages in test group")
    @Test
    public void shouldTrySendingABotMessage() {
        List<Bot> bots = underTest.getBots(groupId);
        Bot bot = bots.get(0);
        String id = bot.getBotId();
        BotMessage message = new BotMessage("Test", id);
        underTest.sendMessage(message);
    }
}
