package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.bean.GroupMeService;
import com.alexjamesmalcolm.dishbot.groupme.Bot;
import com.alexjamesmalcolm.dishbot.groupme.Group;
import com.alexjamesmalcolm.dishbot.groupme.Member;
import com.alexjamesmalcolm.dishbot.groupme.Message;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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

    @Test
    public void checkThatGroupMeIsAvailable() {
        Map json = restTemplate.getForObject(properties.getBaseUrl(), Map.class);
        System.out.println(json);
    }

    @Before
    public void setup() {
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
        Bot bot = underTest.getBot(groupId);
        assertNotNull(bot);
    }

    @Test
    public void shouldGetTenGroupsByDefault() {
        List<Group> groups = underTest.getAllGroups();
        assertThat(groups.size(), is(10));
    }
}
