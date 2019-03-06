package com.alexjamesmalcolm.dishbot.bean;

import com.alexjamesmalcolm.dishbot.groupme.Message;
import com.alexjamesmalcolm.dishbot.physical.Wheel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.Optional;

@Service
public class Interpreter {

    @Resource
    private EntityManager em;

    public Optional<String> respond(Message message) {
        Wheel wheel = em.find(Wheel.class, message.getGroup_id());
        String text = message.getText();
        if (text.equals("!Dishes")) {
            wheel.advanceWheel();
            return Optional.of("Thank you for cleaning the dishes Alex! The next person on dishes is Sicquan");
        } else if (text.equals("!Time")) {
            return Optional.of("");
        }
        return Optional.empty();
    }
}
