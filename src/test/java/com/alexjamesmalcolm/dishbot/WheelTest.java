package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.physical.User;
import com.alexjamesmalcolm.dishbot.physical.Wheel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.List;

import static java.lang.Thread.sleep;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class WheelTest {

    @Mock
    private User userOne;

    @Mock
    private User userTwo;

    @Mock
    private User userThree;

    @Mock
    private User userFour;

    @InjectMocks
    private Wheel underTest;
    private long id;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        underTest = new Wheel(id);
    }

    @Test
    public void shouldAddTwoRoommatesAndGetBackInOrder() {
        underTest.addRoommate(userOne);
        underTest.addRoommate(userTwo);
        List<User> users = underTest.getRoommatesInOrder();
        assertThat(users, contains(userOne, userTwo));
    }

    @Test
    public void shouldSetFineAmountToTenDollars() {
        final long tenDollars = 10;
        underTest.setFineAmount(tenDollars);
        long actualFine = underTest.getFineAmount();
        assertThat(actualFine, is(tenDollars));
    }

    @Test
    public void shouldGetTheFirstRoommateAddedAsTheCurrentRoommate() {
        underTest.addRoommate(userOne);
        underTest.addRoommate(userTwo);
        User currentRoommate = underTest.getCurrentRoommate();
        assertThat(currentRoommate, is(userOne));
    }

    @Test
    public void shouldGetTheNextRoommateAddedAsTheCurrentRoommate() {
        underTest.addRoommate(userOne);
        underTest.addRoommate(userTwo);
        underTest.advanceWheel();
        User currentRoommate = underTest.getCurrentRoommate();
        assertThat(currentRoommate, is(userTwo));
    }

    @Test
    public void shouldMakeACompleteRotationOfWheelToGetTheCurrentRoommate() {
        underTest.addRoommate(userOne);
        underTest.addRoommate(userTwo);
        underTest.advanceWheel();
        underTest.advanceWheel();
        User currentRoommate = underTest.getCurrentRoommate();
        assertThat(currentRoommate, is(userOne));
    }

    @Test
    public void shouldGetUserTwoAsTheNextUser() {
        underTest.addRoommate(userOne);
        underTest.addRoommate(userTwo);
        User nextRoommate = underTest.getRoommateAfter(userOne);
        assertThat(nextRoommate, is(userTwo));
    }

    @Test
    public void shouldGetUserOneAsTheNextUser() {
        underTest.addRoommate(userOne);
        underTest.addRoommate(userTwo);
        User nextRoommate = underTest.getRoommateAfter(userTwo);
        assertThat(nextRoommate, is(userOne));
    }

    @Test
    public void shouldGetFullDurationForUserOneAsFourtyEightHours() {
        Duration duration = Duration.of(48, HOURS);
        underTest.setDurationUntilFine(duration);
        underTest.addRoommate(userOne);
        Duration actual = underTest.getDurationUntilFineForCurrentRoommate();
        assertThat(actual, is(duration));
    }

    @Test
    public void shouldGetFullDurationForUserOneAsTwelveHours() {
        Duration duration = Duration.ofHours(12);
        underTest.setDurationUntilFine(duration);
        underTest.addRoommate(userOne);
        Duration actual = underTest.getDurationUntilFineForCurrentRoommate();
        Duration difference = duration.minus(actual);
        assertThat(difference, lessThan(Duration.ofMillis(100)));
    }

    @Test
    public void shouldGetFullDurationForUserOneAsFourtyEightHoursMinusThreeSecond() throws InterruptedException {
        Duration duration = Duration.ofHours(48);
        underTest.setDurationUntilFine(duration);
        underTest.addRoommate(userOne);
        int milliseconds = 3000;
        sleep(milliseconds);
        Duration actual = underTest.getDurationUntilFineForCurrentRoommate();
        Duration difference = duration.minus(actual).minus(Duration.ofMillis(milliseconds));
        assertThat(difference, lessThan(Duration.ofMillis(100)));
    }

    @Test
    public void shouldGetFullDurationForUserOneAsFourtyEightHoursMinusThreeSecondWhenThereAreTwoUsers() throws InterruptedException {
        Duration duration = Duration.ofHours(48);
        underTest.setDurationUntilFine(duration);
        underTest.addRoommate(userOne);
        int waitTime = 1000;
        sleep(waitTime);
        underTest.addRoommate(userTwo);
        int milliseconds = 3000;
        sleep(milliseconds);
        Duration actual = underTest.getDurationUntilFineForCurrentRoommate();
        Duration difference = duration.minus(actual).minus(Duration.ofMillis(milliseconds + waitTime));
        assertThat(difference, lessThan(Duration.ofMillis(100)));
    }

    @Test
    public void shouldGetFullDurationForUserTwoAsFourtyEightHoursMinusThreeSecondWhenThereAreTwoUsers() throws InterruptedException {
        Duration duration = Duration.ofHours(48);
        underTest.setDurationUntilFine(duration);
        underTest.addRoommate(userOne);
        int waitTime = 3000;
        sleep(waitTime);
        underTest.addRoommate(userTwo);
        underTest.advanceWheel();
        int milliseconds = 4000;
        sleep(milliseconds);
        Duration actual = underTest.getDurationUntilFineForCurrentRoommate();
        Duration difference = duration.minus(actual).minus(Duration.ofMillis(milliseconds));
        assertThat(difference, lessThan(Duration.ofMillis(100)));
    }
}
