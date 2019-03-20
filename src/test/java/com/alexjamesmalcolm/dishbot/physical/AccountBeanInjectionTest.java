package com.alexjamesmalcolm.dishbot.physical;

import com.alexjamesmalcolm.groupme.response.Me;
import com.alexjamesmalcolm.groupme.service.GroupMeService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountBeanInjectionTest {

    @InjectMocks
    private Account underTest;

    @MockBean
    private GroupMeService groupMe;
    private String token;

    @Mock
    private Me me;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        token = "asdf";
        when(groupMe.getMe(token)).thenReturn(me);
    }

    @Test
    public void shouldGetName() {
        Long userId = 1L;
        underTest = new Account(token, userId);
        String expected = "Alex";
        when(me.getName()).thenReturn(expected);
        String name = underTest.getName();
        Assert.assertThat(name, Matchers.is(expected));
    }
}
