package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.bean.Interpreter;
import com.alexjamesmalcolm.dishbot.logical.BotMessage;
import com.alexjamesmalcolm.dishbot.physical.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InterpreterTest {

    private Interpreter underTest = new Interpreter();

    @Mock
    private Message mockMessage;

    @Mock
    private User mockCurrentRoommate;

    @Mock
    private Group mockGroup;

    @Mock
    private Wheel mockWheel;

    @Mock
    private User mockNextRoommate;

    @Mock
    private WheelRepository mockWheelRepo;

    @Mock
    private Bot mockBot;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        underTest.setWheelRepository(mockWheelRepo);
        when(mockMessage.getWheel()).thenReturn(mockWheel);
        when(mockWheel.getCurrentRoommate()).thenReturn(mockCurrentRoommate);
        when(mockWheel.getRoommateAfter(mockCurrentRoommate)).thenReturn(mockNextRoommate);
        when(mockMessage.getGroup()).thenReturn(mockGroup);
        when(mockGroup.getBot()).thenReturn(mockBot);
    }

    @Test
    public void shouldHaveInterpreterThankUserForCompletingDishes() {
        String content = "Dishes done";
        String name = "Alex";
        String nextName = "Sicquan";
        when(mockCurrentRoommate.getName()).thenReturn(name);
        when(mockNextRoommate.getName()).thenReturn(nextName);
        when(mockMessage.getText()).thenReturn(content);
        BotMessage response = underTest.respond(mockMessage).get();
        String expected = MessageFormat.format("Thank you {0}! You''re up on dishes now, {1}.", name, nextName);
        assertThat(response.getText(), is(expected));
    }

    @Test
    public void shouldHaveInterpreterThankSicquanForCompletingDishes() {
        String content = "Dishes done";
        String name = "Sicquan";
        String nextName = "Alex";
        when(mockCurrentRoommate.getName()).thenReturn(name);
        when(mockNextRoommate.getName()).thenReturn(nextName);
        when(mockMessage.getText()).thenReturn(content);
        BotMessage response = underTest.respond(mockMessage).get();
        String expected = MessageFormat.format("Thank you {0}! You''re up on dishes now, {1}.", name, nextName);
        assertThat(response.getText(), is(expected));
    }

    @Test
    public void shouldNotRespondIfContentsAreNotDishesDone() {
        String content = "Is anyone going to the girls house?";
        String name = "Sicquan";
        String nextName = "Alex";
        when(mockCurrentRoommate.getName()).thenReturn(name);
        when(mockNextRoommate.getName()).thenReturn(nextName);
        when(mockMessage.getText()).thenReturn(content);
        Optional<BotMessage> response = underTest.respond(mockMessage);
        assertThat(response.isPresent(), is(false));
    }

    @Test
    public void shouldRespondIfContentsAreIDidTheDishes() {
        String content = "I did the dishes";
        String name = "Sicquan";
        String nextName = "Alex";
        when(mockCurrentRoommate.getName()).thenReturn(name);
        when(mockNextRoommate.getName()).thenReturn(nextName);
        when(mockMessage.getText()).thenReturn(content);
        Optional<BotMessage> response = underTest.respond(mockMessage);
        assertThat(response.isPresent(), is(true));
    }

    @Test
    public void shouldTellWheelToMoveForwards() {
        String content = "I did the dishes";
        String name = "Sicquan";
        String nextName = "Alex";
        when(mockCurrentRoommate.getName()).thenReturn(name);
        when(mockNextRoommate.getName()).thenReturn(nextName);
        when(mockMessage.getText()).thenReturn(content);
        underTest.respond(mockMessage);
        verify(mockWheel).advanceWheel();
    }

    @Test
    public void shouldRecognizeOwnerAndRespondOnlyToTheirAdminCommands() {
    }

    @Test
    public void shouldTellCurrentPersonHowMuchTimeTheyHave() {
        String content = "How much more time do I have to do dishes?";
        String name = "Alex";
        Duration timeLeft = Duration.ofHours(48);
        when(mockCurrentRoommate.getName()).thenReturn(name);
        when(mockMessage.getText()).thenReturn(content);
        when(mockMessage.getUser()).thenReturn(mockCurrentRoommate);
        when(mockWheel.getDurationUntilFineForCurrentRoommate()).thenReturn(timeLeft);
        Optional<BotMessage> potentialResponse = underTest.respond(mockMessage);
        BotMessage response = potentialResponse.get();
        String actual = name + " has " + timeLeft.toHours() + " hour(s) left.";
        assertThat(response.getText(), is(actual));
    }

    @Test
    public void shouldTellSicquanHowMuchTimeHeHasLeft() {
        String content = "How much more time do I have to do dishes?";
        String name = "Sicquan";
        Duration timeLeft = Duration.ofHours(48);
        when(mockCurrentRoommate.getName()).thenReturn(name);
        when(mockMessage.getText()).thenReturn(content);
        when(mockMessage.getUser()).thenReturn(mockCurrentRoommate);
        when(mockWheel.getDurationUntilFineForCurrentRoommate()).thenReturn(timeLeft);
        Optional<BotMessage> potentialResponse = underTest.respond(mockMessage);
        BotMessage response = potentialResponse.get();
        String actual = name + " has " + timeLeft.toHours() + " hour(s) left.";
        assertThat(response.getText(), is(actual));
    }

    @Test
    public void shouldTellAlexHeHasTwelveHoursLeft() {
        String content = "How much more time do I have to do dishes?";
        String name = "Alex";
        Duration timeLeft = Duration.ofHours(12);
        when(mockCurrentRoommate.getName()).thenReturn(name);
        when(mockMessage.getText()).thenReturn(content);
        when(mockMessage.getUser()).thenReturn(mockCurrentRoommate);
        when(mockWheel.getDurationUntilFineForCurrentRoommate()).thenReturn(timeLeft);
        Optional<BotMessage> potentialResponse = underTest.respond(mockMessage);
        BotMessage response = potentialResponse.get();
        String actual = name + " has " + timeLeft.toHours() + " hour(s) left.";
        assertThat(response.getText(), is(actual));
    }

    @Test
    public void shouldUseTheNameOfTheCurrentRoommateRatherThanTheRoommateThatAsked() {
        String content = "How much more time do I have to do dishes?";
        String currentName = "Sicquan";
        Duration timeLeft = Duration.ofHours(12);
        when(mockCurrentRoommate.getName()).thenReturn(currentName);
        when(mockMessage.getText()).thenReturn(content);
        when(mockMessage.getUser()).thenReturn(mockNextRoommate);
        when(mockWheel.getDurationUntilFineForCurrentRoommate()).thenReturn(timeLeft);
        Optional<BotMessage> potentialResponse = underTest.respond(mockMessage);
        BotMessage response = potentialResponse.get();
        String actual = currentName + " has " + timeLeft.toHours() + " hour(s) left.";
        assertThat(response.getText(), is(actual));
    }

    @Test
    public void shouldAttachCorrectBotIdToBotMessage() {
        String content = "Dishes done";
        String name = "Alex";
        String nextName = "Sicquan";
        String botId = "asdf";
        when(mockCurrentRoommate.getName()).thenReturn(name);
        when(mockNextRoommate.getName()).thenReturn(nextName);
        when(mockMessage.getText()).thenReturn(content);
        when(mockBot.getId()).thenReturn(botId);
        BotMessage response = underTest.respond(mockMessage).get();
        assertThat(response.getBotId(), is(botId));
    }

    @Test
    public void shouldAttachCorrectBotIdToBotMessageAsOneHundered() {
        String content = "Dishes done";
        String name = "Alex";
        String nextName = "Sicquan";
        String botId = "100";
        when(mockCurrentRoommate.getName()).thenReturn(name);
        when(mockNextRoommate.getName()).thenReturn(nextName);
        when(mockMessage.getText()).thenReturn(content);
        when(mockBot.getId()).thenReturn(botId);
        BotMessage response = underTest.respond(mockMessage).get();
        assertThat(response.getBotId(), is(botId));
    }
}
