package com.alexjamesmalcolm.dishbot.bean;

import com.alexjamesmalcolm.dishbot.groupme.Message;
import com.alexjamesmalcolm.dishbot.physical.Wheel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterpreterTest {

    @Resource
    private GroupMeService groupMe;

    @InjectMocks
    @Resource
    private Interpreter underTest;

    @Mock
    private Message message;

    @Mock
    private Wheel wheel;

    @MockBean
    private EntityManager em;

    private long groupId = 1234;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(em.find(Wheel.class, groupId)).thenReturn(wheel);
        when(message.getGroup_id()).thenReturn(groupId);
    }

    @Test
    public void shouldNotHaveNullInterpreter() {
        assertNotNull(underTest);
    }

    @Test
    public void shouldNotHaveGroupMeServiceBeNull() {
        assertNotNull(groupMe);
    }

    @Test
    public void shouldRespondToBangDishes() {
        String text = "!Dishes";
        when(message.getText()).thenReturn(text);
        String response = underTest.respond(message).get();
        String expected = "Thank you for cleaning the dishes Alex! The next person on dishes is Sicquan";
        assertThat(response, is(expected));
    }

    @Test
    public void shouldAdvanceWheel() {
        String text = "!Dishes";
        when(message.getText()).thenReturn(text);
        underTest.respond(message);
        verify(wheel).advanceWheel();
    }

    @Test
    public void shouldNotAdvanceWheelIfCommandIsNotDishes() {
        String text = "!Time";
        when(message.getText()).thenReturn(text);
        underTest.respond(message);
        verify(wheel, never()).advanceWheel();
    }

    @Test
    public void shouldNotThankUserForDoingDishesIfCommandIsNotDishes() {
        String text = "!Time";
        when(message.getText()).thenReturn(text);
        String response = underTest.respond(message).get();
        String expected = "Thank you for cleaning the dishes Alex! The next person on dishes is Sicquan";
        assertThat(response, is(not(expected)));
    }

    @Test
    public void shouldReturnNoResponseIfNotACommand() {
        String text = "Hello there";
        when(message.getText()).thenReturn(text);
        Optional<String> potentialResponse = underTest.respond(message);
        assertFalse(potentialResponse.isPresent());
    }
}
