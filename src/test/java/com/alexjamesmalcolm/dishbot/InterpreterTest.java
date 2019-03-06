package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.bean.GroupMeService;
import com.alexjamesmalcolm.dishbot.bean.Interpreter;
import com.alexjamesmalcolm.dishbot.groupme.Message;
import com.alexjamesmalcolm.dishbot.logical.BotMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterpreterTest {

    @Resource
    private GroupMeService groupMe;

    @Resource
    private Interpreter interpreter;

    @Mock
    private Message message;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldNotHaveNullInterpreter() {
        assertNotNull(interpreter);
    }

    @Test
    public void shouldNotHaveGroupMeServiceBeNull() {
        assertNotNull(groupMe);
    }

    @Test
    public void shouldRespondToBangDishes() {
        String text = "!Dishes";
        Mockito.when(message.getText()).thenReturn(text);
        BotMessage response = interpreter.respond(message);
        String expected = "Thank you for cleaning the dishes Alex! The next person on dishes is Sicquan";
        assertThat(response.getText(), is(expected));
    }
}
