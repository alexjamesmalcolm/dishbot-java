package com.alexjamesmalcolm.dishbot.physical;

import com.alexjamesmalcolm.dishbot.groupme.Member;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

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

    private long groupId = 123;

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
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        long userId = wheel.getCurrentMemberUserId();
        assertThat(userId, is(memberOne.getUserId()));
    }

    @Test
    public void shouldAddTwoPeopleToWheelAndTheFirstPersonShouldBeCurrent() {
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        wheel.addMember(memberTwo);
        long userId = wheel.getCurrentMemberUserId();
        assertThat(userId, is(memberOne.getUserId()));
    }

    @Test
    public void shouldAdvanceTheWheelAndTheSecondPersonShouldBeCurrent() {
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        wheel.addMember(memberTwo);
        wheel.advanceWheel();
        long userId = wheel.getCurrentMemberUserId();
        assertThat(userId, is(memberTwo.getUserId()));
    }

    @Test
    public void shouldWrapAroundWhenAdvancingTwiceWithTwoPeople() {
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
        long expected = 48L;
        Wheel wheel = new Wheel(groupId);
        Duration actual = wheel.getFineDuration();
        assertThat(actual.toHours(), is(expected));
    }

    @Test
    public void shouldBeAbleToSetTheFineDurationToTenHoursFromConstructor() {
        long expected = 10;
        Wheel wheel = new Wheel(groupId, Duration.ofHours(expected));
        Duration actual = wheel.getFineDuration();
        assertThat(actual.toHours(), is(expected));
    }

    @Test
    public void shouldBeAbleToSetTheFineDurationToEightHoursFromSetter() {
        long expected = 8;
        Wheel wheel = new Wheel(groupId);
        Duration fineDuration = Duration.ofHours(expected);
        wheel.setFineDuration(fineDuration);
        Duration actual = wheel.getFineDuration();
        assertThat(actual, is(fineDuration));
    }

    @Test
    public void shouldGetNullForTimeUntilFineIfThereIsNoCurrentMember() {
        Wheel wheel = new Wheel(groupId);
        Duration actual = wheel.getDurationUntilFineForCurrent();
        assertNull(actual);
    }

    @Test
    public void shouldGetFourtyEightHoursUntilFineForCurrentMember() {
        long expected = 48;
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        Duration actual = wheel.getDurationUntilFineForCurrent();
        assertThat(actual.toHours(), is(expected));
    }

    @Test
    public void shouldGetTenHoursUntilFineForCurrentMember() {
        long expected = 10;
        Wheel wheel = new Wheel(groupId, Duration.ofHours(10));
        wheel.addMember(memberOne);
        Duration actual = wheel.getDurationUntilFineForCurrent();
        assertThat(actual.toHours(), is(expected));
    }

    @Test
    public void shouldGetOneSecondLessThanFourtyEightHoursUntilFineForCurrentMember() throws InterruptedException {
        BigDecimal expected = new BigDecimal(1000);
        Duration fineDuration = Duration.ofHours(48);
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        sleep(expected.intValue());
        Duration actual = wheel.getDurationUntilFineForCurrent();
        Duration difference = fineDuration.minus(actual);
        assertThat(BigDecimal.valueOf(difference.toMillis()), is(Matchers.closeTo(expected, BigDecimal.valueOf(10))));
    }

    @Test
    public void shouldGetDefaultFineOfFiveDollars() {
        Double expected = 5.0;
        Wheel wheel = new Wheel(groupId);
        Double fine = wheel.getFineAmount();
        assertThat(fine, is(expected));
    }

    @Test
    public void shouldGetConstructorSetTenDollars() {
        Double expected = 10.0;
        Wheel wheel = new Wheel(groupId, expected);
        Double fine = wheel.getFineAmount();
        assertThat(fine, is(expected));
    }

    @Test
    public void shouldAddMemberAndThenGetTheirFineAsZero() {
        Double expected = 0.0;
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        Map<Long, Double> fineAmounts = wheel.getFineAmounts();
        Double actual = fineAmounts.get(memberOne.getUserId());
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldHaveMemberNotBeExpired() {
        boolean expected = false;
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        boolean actual = wheel.isExpired();
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldHaveMemberBeExpired() throws InterruptedException {
        boolean expected = true;
        int sleepTime = 1;
        Duration fineDuration = Duration.ofMillis(sleepTime);
        Wheel wheel = new Wheel(groupId, fineDuration);
        wheel.addMember(memberOne);
        wheel.addMember(memberTwo);
        sleep(sleepTime + 1);
        boolean actual = wheel.isExpired();
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldBeAbleToGiveMemberOneAFine() {
        Double expected = 5.0;
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        wheel.addMember(memberTwo);
        wheel.giveFine(memberOne);
        Map<Long, Double> fineAmounts = wheel.getFineAmounts();
        Double actual = fineAmounts.get(memberOne.getUserId());
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldBeAbleToGiveMemberTwoFines() {
        Double expected = 10.0;
        Wheel wheel = new Wheel(groupId);
        wheel.addMember(memberOne);
        wheel.addMember(memberTwo);
        wheel.giveFine(memberOne);
        wheel.giveFine(memberOne.getUserId());
        Map<Long, Double> fineAmounts = wheel.getFineAmounts();
        Double actual = fineAmounts.get(memberOne.getUserId());
        assertThat(actual, is(expected));
    }
}
