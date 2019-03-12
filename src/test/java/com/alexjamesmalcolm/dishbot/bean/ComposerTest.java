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

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.Long.parseLong;
import static java.math.BigDecimal.ONE;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class ComposerTest {

    @InjectMocks
    private Composer underTest;

    @Mock
    private GroupMeService groupMe;

    @Mock
    private EntityManager em;

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
        underTest = new Composer();
        MockitoAnnotations.initMocks(this);
        when(message.getGroupId()).thenReturn(groupId);
        when(groupMe.getGroup(message)).thenReturn(group);
        when(currentMember.getUserId()).thenReturn(currentMemberUserId);
        when(nextMember.getUserId()).thenReturn(nextMemberUserId);
        when(group.queryForMember(currentMemberUserId)).thenReturn(Optional.of(currentMember));
        when(group.queryForMember(nextMemberUserId)).thenReturn(Optional.of(nextMember));
        wheel = new Wheel(groupId);
        wheel.addMember(currentMember);
        wheel.addMember(nextMember);
        when(wheelRepo.findByGroupId(message.getGroupId())).thenReturn(Optional.of(wheel));
        when(wheelRepo.findByGroupId(groupId)).thenReturn(Optional.of(wheel));
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
    public void shouldGiveFourtyEightHoursToDoTheDishes() {
        String text = "!Time";
        when(message.getText()).thenReturn(text);
        when(message.getUserId()).thenReturn(currentMemberUserId);
        String currentName = "Alex";
        when(currentMember.getName()).thenReturn(currentName);
        String response = underTest.respond(message).get();
        List<String> words = Arrays.asList(response.split(" "));
        long actualHours = parseLong(words.get(words.indexOf("has") + 1));
        assertThat(BigDecimal.valueOf(actualHours), is(closeTo(BigDecimal.valueOf(48), ONE)));
    }

    @Test
    public void shouldHaveSicquanDoTheDishes() {
        String text = "!Time";
        when(message.getText()).thenReturn(text);
        when(message.getUserId()).thenReturn(currentMemberUserId);
        String currentName = "Sicquan";
        when(currentMember.getName()).thenReturn(currentName);
        String response = underTest.respond(message).get();
        List<String> words = Arrays.asList(response.split(" "));
        String actualName = words.get(0);
        assertThat(actualName, is(currentName));
    }

    @Test
    public void shouldHaveAlexDoTheDishes() {
        String text = "!Time";
        when(message.getText()).thenReturn(text);
        when(message.getUserId()).thenReturn(currentMemberUserId);
        String currentName = "Alex";
        long hours = 10;
        wheel = new Wheel(groupId, Duration.ofHours(hours));
        wheel.addMember(currentMember);
        wheel.addMember(nextMember);
        when(wheelRepo.findByGroupId(message.getGroupId())).thenReturn(Optional.of(wheel));
        when(wheelRepo.findByGroupId(groupId)).thenReturn(Optional.of(wheel));
        when(currentMember.getName()).thenReturn(currentName);
        String response = underTest.respond(message).get();
        List<String> words = Arrays.asList(response.split(" "));
        String actualName = words.get(0);
        assertThat(actualName, is(currentName));
    }

    @Test
    public void shouldGiveTenHoursToDoTheDishes() {
        String text = "!Time";
        when(message.getText()).thenReturn(text);
        when(message.getUserId()).thenReturn(currentMemberUserId);
        String currentName = "Alex";
        long hours = 10;
        wheel = new Wheel(groupId, Duration.ofHours(hours));
        wheel.addMember(currentMember);
        wheel.addMember(nextMember);
        when(wheelRepo.findByGroupId(message.getGroupId())).thenReturn(Optional.of(wheel));
        when(wheelRepo.findByGroupId(groupId)).thenReturn(Optional.of(wheel));
        when(currentMember.getName()).thenReturn(currentName);
        String response = underTest.respond(message).get();
        List<String> words = Arrays.asList(response.split(" "));
        long actualHours = parseLong(words.get(words.indexOf("has") + 1));
        assertThat(BigDecimal.valueOf(actualHours), is(closeTo(BigDecimal.valueOf(hours), ONE)));
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

        String expected = "User IDs:\n" + currentName + " -> " + currentMemberUserId + "\n" + nextName + " -> " + nextMemberUserId;
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

        String expected = "User IDs:\n" + currentName + " -> " + currentMemberUserId + "\n" + nextName + " -> " + nextMemberUserId;
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
        when(group.queryForMember(currentMemberUserId)).thenReturn(Optional.of(currentMember));
        when(wheelRepo.findByGroupId(group.getGroupId())).thenReturn(Optional.of(wheel));

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
        when(group.queryForMember(currentMemberUserId)).thenReturn(Optional.of(currentMember));
        when(wheelRepo.findByGroupId(group.getGroupId())).thenReturn(Optional.of(wheel));

        String response = underTest.respond(message).get();

        String expected = "Added " + currentName + " to Dish Wheel.";
        assertThat(response, is(expected));
    }

    @Test
    public void shouldRespondWithErrorMessageIfIdIsNotFoundInGroup() {
        long badId = 432198764321L;
        String text = "!Add " + badId;
        when(message.getText()).thenReturn(text);
        when(wheelRepo.findByGroupId(group.getGroupId())).thenReturn(Optional.of(wheel));

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
        when(group.queryForMember(currentMemberUserId)).thenReturn(Optional.of(currentMember));
        wheel = new Wheel(groupId);
        when(wheelRepo.findByGroupId(message.getGroupId())).thenReturn(Optional.of(wheel));
        when(wheelRepo.findByGroupId(groupId)).thenReturn(Optional.of(wheel));
        when(wheelRepo.findByGroupId(group.getGroupId())).thenReturn(Optional.of(wheel));

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
        when(group.queryForMember(currentMemberUserId)).thenReturn(Optional.of(currentMember));
        wheel = new Wheel(groupId);
        when(wheelRepo.findByGroupId(message.getGroupId())).thenReturn(Optional.of(wheel));
        when(wheelRepo.findByGroupId(groupId)).thenReturn(Optional.of(wheel));
        when(wheelRepo.findByGroupId(group.getGroupId())).thenReturn(Optional.of(wheel));

        underTest.respond(message).get();

        long actual = wheel.getCurrentMemberUserId();
        assertThat(actual, is(not(badId)));
    }
}
