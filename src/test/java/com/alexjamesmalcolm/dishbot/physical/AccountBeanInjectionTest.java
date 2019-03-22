package com.alexjamesmalcolm.dishbot.physical;

import com.alexjamesmalcolm.groupme.response.Group;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static java.util.stream.Collectors.joining;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountBeanInjectionTest {

    private Account underTest;

    private String token;
    private Long userId;

    @Before
    public void setup() {
        token = "1da573f0282d013738075ad8d3cabcd7";
        userId = 19742906L;
    }

    @Test
    public void shouldGetName() {
        underTest = new Account(token, userId);
        String expected = "Alex Malcolm";
        String name = underTest.getName();
        assertThat(name, is(expected));
    }

    @Test
    public void shouldGetGroupsWithoutWheels() {
        underTest = new Account(token, userId);
        Collection<Group> groups = underTest.getGroupsWithoutWheels();
        assertTrue("There should be more than 2 groups but these are the groups we found instead: " + groups.stream().map(Group::getName).collect(joining(", ")), groups.size() > 2);
    }
}
