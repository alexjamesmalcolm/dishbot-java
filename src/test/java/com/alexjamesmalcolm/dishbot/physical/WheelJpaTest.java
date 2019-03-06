package com.alexjamesmalcolm.dishbot.physical;

import com.alexjamesmalcolm.dishbot.physical.Wheel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WheelJpaTest {

    @Resource
    private TestEntityManager em;

    @Test
    public void shouldSaveWheel() {
        Wheel wheel = new Wheel();
        wheel = em.persist(wheel);
        long id = wheel.getId();
        em.flush();
        em.clear();
        wheel = em.find(Wheel.class, id);
        assertThat(wheel.getId(), is(1L));
    }

    @Test
    public void shouldSaveAndRetrieveTwoWheels() {
        Wheel firstWheel = new Wheel();
        Wheel secondWheel = new Wheel();
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
}
