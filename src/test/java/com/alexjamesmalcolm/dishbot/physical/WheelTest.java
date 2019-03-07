package com.alexjamesmalcolm.dishbot.physical;

import com.alexjamesmalcolm.dishbot.groupme.Member;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;

import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class WheelTest {

    @Mock
    private Member memberOne;
    private long firstUserId;

    @Mock
    private Member memberTwo;
    private long secondUserId;

    @Before
    public void setup() {
        firstUserId = 100L;
        secondUserId = 200L;
        MockitoAnnotations.initMocks(this);
        when(memberOne.getUserId()).thenReturn(firstUserId);
        when(memberTwo.getUserId()).thenReturn(secondUserId);
    }

    @Test
    public void shouldGetCurrentMemberAsAddedMember() {
        long groupId = 123;
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        long userId = wheel.getCurrentMemberUserId();
        assertThat(userId, is(memberOne.getUserId()));
    }

    @Test
    public void shouldAddTwoPeopleToWheelAndTheFirstPersonShouldBeCurrent() {
        long groupId = 123;
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        wheel.addMember(memberTwo);
        long userId = wheel.getCurrentMemberUserId();
        assertThat(userId, is(memberOne.getUserId()));
    }

    @Test
    public void shouldAdvanceTheWheelAndTheSecondPersonShouldBeCurrent() {
        long groupId = 123;
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        wheel.addMember(memberTwo);
        wheel.advanceWheel();
        long userId = wheel.getCurrentMemberUserId();
        assertThat(userId, is(memberTwo.getUserId()));
    }

    @Test
    public void shouldWrapAroundWhenAdvancingTwiceWithTwoPeople() {
        long groupId = 123;
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        wheel.addMember(memberTwo);
        wheel.advanceWheel();
        wheel.advanceWheel();
        long userId = wheel.getCurrentMemberUserId();
        assertThat(userId, is(memberOne.getUserId()));
    }

    @Test
    public void shouldGetDefaultFineDurationOfFourtyEightHours() {
        long groupId = 123;
        long expected = 48L;
        Wheel wheel = new Wheel(groupId);
        Duration actual = wheel.getFineDuration();
        assertThat(actual.toHours(), is(expected));
    }

    @Test
    public void shouldBeAbleToSetTheFineDurationToTenHoursFromConstructor() {
        long groupId = 123;
        long expected = 10;
        Wheel wheel = new Wheel(groupId, Duration.ofHours(expected));
        Duration actual = wheel.getFineDuration();
        assertThat(actual.toHours(), is(expected));
    }

    @Test
    public void shouldBeAbleToSetTheFineDurationToEightHoursFromSetter() {
        long groupId = 123;
        long expected = 8;
        Wheel wheel = new Wheel(groupId);
        Duration fineDuration = Duration.ofHours(expected);
        wheel.setFineDuration(fineDuration);
        Duration actual = wheel.getFineDuration();
        assertThat(actual, is(fineDuration));
    }

    @Test
    public void shouldGetNullForTimeUntilFineIfThereIsNoCurrentMember() {
        long groupId = 123;
        Wheel wheel = new Wheel(groupId);
        Duration actual = wheel.getDurationUntilFineForCurrent();
        assertNull(actual);
    }

    @Test
    public void shouldGetFourtyEightHoursUntilFineForCurrentMember() {
        long groupId = 123;
        long expected = 48;
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        Duration actual = wheel.getDurationUntilFineForCurrent();
        assertThat(actual.toHours(), is(expected));
    }

    @Test
    public void shouldGetTenHoursUntilFineForCurrentMember() {
        long groupId = 123;
        long expected = 10;
        Wheel wheel = new Wheel(groupId, Duration.ofHours(10));
        wheel.addMember(memberOne);
        Duration actual = wheel.getDurationUntilFineForCurrent();
        assertThat(actual.toHours(), is(expected));
    }

    @Test
    public void shouldGetOneSecondLessThanFourtyEightHoursUntilFineForCurrentMember() throws InterruptedException {
        long groupId = 123;
        long expected = 1000;
        Duration fineDuration = Duration.ofHours(48);
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        sleep(expected);
        Duration actual = wheel.getDurationUntilFineForCurrent();
        Duration difference = fineDuration.minus(actual);
        assertThat(difference.toMillis(), is(expected));
    }
}