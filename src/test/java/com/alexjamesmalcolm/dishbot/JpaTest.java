package com.alexjamesmalcolm.dishbot;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JpaTest {

    @Resource
    private TestEntityManager em;

    @Resource
    private MessageRepository messageRepo;

    @Ignore
    @Test
    public void shouldSaveMessage() {
//        long id = messageRepo.save(message).getId();
        em.flush();
        em.clear();
    }
}