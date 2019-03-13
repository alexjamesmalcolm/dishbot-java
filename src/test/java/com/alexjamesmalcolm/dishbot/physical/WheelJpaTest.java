package com.alexjamesmalcolm.dishbot.physical;

import com.alexjamesmalcolm.dishbot.AccountRepository;
import com.alexjamesmalcolm.dishbot.WheelRepository;
import com.alexjamesmalcolm.groupme.response.Group;
import com.alexjamesmalcolm.groupme.response.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WheelJpaTest {

    @Resource
    private TestEntityManager em;

    @Resource
    private WheelRepository wheelRepo;

    @Resource
    private AccountRepository accountRepo;

    @Mock
    private Member memberOne;

    private Account owner;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        String accessToken = "";
        Long userId = 1L;
        Collection<Group> groups = new ArrayList<>();
        owner = new Account(accessToken, userId, groups);
        owner = accountRepo.save(owner);
    }

    @Test
    public void shouldSaveWheel() {
        Wheel wheel = new Wheel(owner, 123);
        wheel = em.persist(wheel);
        long id = wheel.getId();
        em.flush();
        em.clear();
        wheel = em.find(Wheel.class, id);
        assertThat(wheel.getId(), is(id));
    }

    @Test
    public void shouldSaveAndRetrieveTwoWheels() {
        Wheel firstWheel = new Wheel(owner, 123);
        Wheel secondWheel = new Wheel(owner, 123);
        firstWheel = em.persist(firstWheel);
        secondWheel = em.persist(secondWheel);
        long firstId = firstWheel.getId();
        long secondId = secondWheel.getId();
        em.flush();
        em.clear();
        firstWheel = em.find(Wheel.class, firstId);
        secondWheel = em.find(Wheel.class, secondId);
        assertThat(firstWheel, is(not(secondWheel)));
    }

    @Test
    public void shouldBeAbleToFindWheelByGroupId() {
        long groupId = 123;
        Wheel wheel = new Wheel(owner, groupId);
        em.persist(wheel);
        em.flush();
        em.clear();
        Optional<Wheel> potentialWheel = wheelRepo.findByGroupId(groupId);
        wheel = potentialWheel.get();
        assertNotNull(wheel);
    }

    @Test
    public void shouldGetCurrentMemberAsAddedMember() {
        long groupId = 123;
        Wheel wheel = new Wheel(owner, groupId);
        when(memberOne.getUserId()).thenReturn(20L);
        wheel.addMember(memberOne);
        em.persist(wheel);
        em.flush();
        em.clear();
        Optional<Wheel> potentialWheel = wheelRepo.findByGroupId(groupId);
        wheel = potentialWheel.get();
        long userId = wheel.getCurrentMemberUserId();
        assertThat(userId, is(memberOne.getUserId()));
    }
}
