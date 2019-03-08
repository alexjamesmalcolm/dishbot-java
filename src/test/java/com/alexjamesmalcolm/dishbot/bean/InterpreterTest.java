package com.alexjamesmalcolm.dishbot.bean;

import com.alexjamesmalcolm.dishbot.WheelRepository;
import com.alexjamesmalcolm.dishbot.groupme.Group;
import com.alexjamesmalcolm.dishbot.groupme.Member;
import com.alexjamesmalcolm.dishbot.groupme.Message;
import com.alexjamesmalcolm.dishbot.physical.Wheel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class InterpreterTest {

    @InjectMocks
    private Interpreter underTest;

    @Mock
    private GroupMeService groupMe;

    @Mock
    private Message message;

    @Mock
    private WheelRepository wheelRepo;

    @Mock
    private Member currentMember;
    private long currentMemberUserId = 123;

    @Mock
    private Member nextMember;
    private long nextMemberUserId = 321;

    @Mock
    private Group group;
    private long groupId = 1234;

    private Wheel wheel;

    @Before
    public void setup() {
        underTest = new Interpreter();
        MockitoAnnotations.initMocks(this);
        when(message.getGroupId()).thenReturn(groupId);
        when(groupMe.getGroup(message)).thenReturn(group);
        when(currentMember.getUserId()).thenReturn(currentMemberUserId);
        when(nextMember.getUserId()).thenReturn(nextMemberUserId);
        when(group.getMember(currentMemberUserId)).thenReturn(Optional.of(currentMember));
        when(group.getMember(nextMemberUserId)).thenReturn(Optional.of(nextMember));
        wheel = new Wheel(groupId);
        wheel.addMember(currentMember);
        wheel.addMember(nextMember);
        when(wheelRepo.findByGroupId(message.getGroupId())).thenReturn(wheel);
        when(wheelRepo.findByGroupId(groupId)).thenReturn(wheel);
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
        String expected = "Thank you for cleaning the dishes Alex! The next person on dishes is Sicquan.";
        String currentName = "Alex";
        String nextName = "Sicquan";
        when(currentMember.getName()).thenReturn(currentName);
        when(nextMember.getName()).thenReturn(nextName);
        String response = underTest.respond(message).get();
        assertThat(response, is(expected));
    }

    @Test
    public void shouldNotThankUserForDoingDishesIfCommandIsNotDishes() {
        String text = "!Time";
        when(message.getText()).thenReturn(text);
        when(message.getUserId()).thenReturn(currentMemberUserId);
        String response = underTest.respond(message).get();
        String expected = "Thank you for cleaning the dishes Alex! The next person on dishes is Sicquan.";
        assertThat(response, is(not(expected)));
    }

    @Test
    public void shouldReturnNoResponseIfNotACommand() {
        String text = "Hello there";
        when(message.getText()).thenReturn(text);
        Optional<String> potentialResponse = underTest.respond(message);
        assertFalse(potentialResponse.isPresent());
    }

    @Test
    public void shouldThankSicquanForDoingTheDishesAndSayThatAlexIsUpNext() {
        String text = "!Dishes";
        when(message.getText()).thenReturn(text);
        String expected = "Thank you for cleaning the dishes Sicquan! The next person on dishes is Alex.";
        String currentName = "Sicquan";
        String nextName = "Alex";
        when(currentMember.getName()).thenReturn(currentName);
        when(nextMember.getName()).thenReturn(nextName);
        String response = underTest.respond(message).get();
        assertThat(response, is(expected));
    }

    @Test
    public void shouldHaveAlexGetFourtyEightHoursToDoTheDishes() {
        String text = "!Time";
        when(message.getText()).thenReturn(text);
        when(message.getUserId()).thenReturn(currentMemberUserId);
        String currentName = "Alex";
        when(currentMember.getName()).thenReturn(currentName);
        String expected = currentName + " has 48 hours to do the dishes.";
        String response = underTest.respond(message).get();
        assertThat(response, is(expected));
    }

    @Test
    public void shouldHaveSicquanGetFourtyEightHoursToDoTheDishes() {
        String text = "!Time";
        when(message.getText()).thenReturn(text);
        when(message.getUserId()).thenReturn(currentMemberUserId);
        String currentName = "Sicquan";
        when(currentMember.getName()).thenReturn(currentName);
        String expected = currentName + " has 48 hours to do the dishes.";
        String response = underTest.respond(message).get();
        assertThat(response, is(expected));
    }

    @Test
    public void shouldHaveAlexGetTenHoursToDoTheDishes() {
        String text = "!Time";
        when(message.getText()).thenReturn(text);
        when(message.getUserId()).thenReturn(currentMemberUserId);
        String currentName = "Alex";
        long hours = 10;
        wheel = new Wheel(groupId, Duration.ofHours(hours));
        wheel.addMember(currentMember);
        wheel.addMember(nextMember);
        when(wheelRepo.findByGroupId(message.getGroupId())).thenReturn(wheel);
        when(wheelRepo.findByGroupId(groupId)).thenReturn(wheel);
        when(currentMember.getName()).thenReturn(currentName);
        String expected = currentName + " has " + hours + " hours to do the dishes.";
        String response = underTest.respond(message).get();
        assertThat(response, is(expected));
    }

    @Test
    public void shouldRespondWithTheUserIdsOfAlexAndSicquan() {
        String text = "!Ids";
        when(message.getText()).thenReturn(text);
        String currentName = "Alex";
        String nextName = "Sicquan";
        when(currentMember.getName()).thenReturn(currentName);
        when(nextMember.getName()).thenReturn(nextName);
        List<Member> members = Arrays.asList(currentMember, nextMember);
        when(group.getMembers()).thenReturn(members);

        String response = underTest.respond(message).get();

        String expected = currentName + " -> " + currentMemberUserId + "\n" + nextName + " -> " + nextMemberUserId;
        assertThat(response, is(expected));
    }

    @Test
    public void shouldRespondWithTheUserIdsOfHanaanAndKwinton() {
        String text = "!Ids";
        when(message.getText()).thenReturn(text);
        String currentName = "Hanaan";
        String nextName = "Kwinton";
        when(currentMember.getName()).thenReturn(currentName);
        when(nextMember.getName()).thenReturn(nextName);
        List<Member> members = Arrays.asList(currentMember, nextMember);
        when(group.getMembers()).thenReturn(members);

        String response = underTest.respond(message).get();

        String expected = currentName + " -> " + currentMemberUserId + "\n" + nextName + " -> " + nextMemberUserId;
        assertThat(response, is(expected));
    }

    @Test
    public void shouldGetProperResponseWhenAddingUserToWheelById() {
        String text = "!Add " + currentMemberUserId;
        when(message.getText()).thenReturn(text);
        String currentName = "Alex";
        when(currentMember.getName()).thenReturn(currentName);
        List<Member> members = Arrays.asList(currentMember, nextMember);
        when(group.getMembers()).thenReturn(members);
        when(group.getMember(currentMemberUserId)).thenReturn(Optional.of(currentMember));
        when(wheelRepo.findByGroupId(group.getGroupId())).thenReturn(wheel);

        String response = underTest.respond(message).get();

        String expected = "Added " + currentName + " to Dish Wheel.";
        assertThat(response, is(expected));
    }

    @Test
    public void shouldGetHanaanAsBeingAddedToWheelById() {
        String text = "!Add " + currentMemberUserId;
        when(message.getText()).thenReturn(text);
        String currentName = "Hanaan";
        when(currentMember.getName()).thenReturn(currentName);
        List<Member> members = Arrays.asList(currentMember, nextMember);
        when(group.getMembers()).thenReturn(members);
        when(group.getMember(currentMemberUserId)).thenReturn(Optional.of(currentMember));
        when(wheelRepo.findByGroupId(group.getGroupId())).thenReturn(wheel);

        String response = underTest.respond(message).get();

        String expected = "Added " + currentName + " to Dish Wheel.";
        assertThat(response, is(expected));
    }

    @Test
    public void shouldRespondWithErrorMessageIfIdIsNotFoundInGroup() {
        long badId = 432198764321L;
        String text = "!Add " + badId;
        when(message.getText()).thenReturn(text);
        when(wheelRepo.findByGroupId(group.getGroupId())).thenReturn(wheel);

        String response = underTest.respond(message).get();

        String expected = "Could not find user with id " + badId;
        assertThat(response, is(expected));
    }

    @Test
    public void shouldAddUserToWheel() {
        String text = "!Add " + currentMemberUserId;
        when(message.getText()).thenReturn(text);
        String currentName = "Hanaan";
        when(currentMember.getName()).thenReturn(currentName);
        List<Member> members = Arrays.asList(currentMember, nextMember);
        when(group.getMembers()).thenReturn(members);
        when(group.getMember(currentMemberUserId)).thenReturn(Optional.of(currentMember));
        wheel = new Wheel(groupId);
        when(wheelRepo.findByGroupId(message.getGroupId())).thenReturn(wheel);
        when(wheelRepo.findByGroupId(groupId)).thenReturn(wheel);
        when(wheelRepo.findByGroupId(group.getGroupId())).thenReturn(wheel);

        underTest.respond(message).get();

        long actual = wheel.getCurrentMemberUserId();
        assertThat(actual, is(currentMemberUserId));
    }

    @Test
    public void shouldNotAddUserToWheelIfIdIsNotFoundInGroup() {
        long badId = 432198764321L;
        String text = "!Add " + badId;
        when(message.getText()).thenReturn(text);
        String currentName = "Hanaan";
        when(currentMember.getName()).thenReturn(currentName);
        List<Member> members = Arrays.asList(currentMember, nextMember);
        when(group.getMembers()).thenReturn(members);
        when(group.getMember(currentMemberUserId)).thenReturn(Optional.of(currentMember));
        wheel = new Wheel(groupId);
        when(wheelRepo.findByGroupId(message.getGroupId())).thenReturn(wheel);
        when(wheelRepo.findByGroupId(groupId)).thenReturn(wheel);
        when(wheelRepo.findByGroupId(group.getGroupId())).thenReturn(wheel);

        underTest.respond(message).get();

        long actual = wheel.getCurrentMemberUserId();
        assertThat(actual, is(not(badId)));
    }
}
