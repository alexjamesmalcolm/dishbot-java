package com.alexjamesmalcolm.dishbot.bean;

import com.alexjamesmalcolm.dishbot.physical.Account;
import com.alexjamesmalcolm.groupme.response.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComposerAopTest {

    @Resource
    private Composer underTest;

    @Mock
    private Account owner;

    @Mock
    private Message message;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void nonOwnerShouldNotBeAbleToUserAddCommand() {
        String text = "!add ";
        when(message.getText()).thenReturn(text);
        when(message.getUserId()).thenReturn(123L);
        when(owner.getUserId()).thenReturn(321L);
        Optional<String> response = underTest.respond(owner, message);
        assertFalse(response.isPresent());
    }
}
