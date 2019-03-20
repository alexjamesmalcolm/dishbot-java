package com.alexjamesmalcolm.dishbot.physical;

import com.alexjamesmalcolm.dishbot.Properties;
import com.alexjamesmalcolm.groupme.service.GroupMeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WheelBeanInjectionTest {

    @Mock
    private Account account;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGetGroupMeService() {
        long groupId = 1234;
        Wheel underTest = new Wheel(account, groupId);
        GroupMeService actual = underTest.getGroupMeService();
        assertNotNull(actual);
    }

    @Test
    public void shouldGetProperties() {
        long groupId = 1234;
        Wheel underTest = new Wheel(account, groupId);
        Properties actual = underTest.getProperties();
        assertNotNull(actual);
    }
}
