package com.alexjamesmalcolm.dishbot.bean;

import com.alexjamesmalcolm.dishbot.WheelRepository;
import com.alexjamesmalcolm.dishbot.groupme.Message;
import com.alexjamesmalcolm.dishbot.physical.Wheel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Optional;

@Service
public class Interpreter {

    @Resource
    private WheelRepository wheelRepo;

    public Optional<String> respond(Message message) {
        Wheel wheel = wheelRepo.findByGroupId(message.getGroupId());
        String text = message.getText();
        if (text.equals("!Dishes")) {
            wheel.advanceWheel();
            String firstName = "Alex";
            String secondName = "Sicquan";
            return Optional.of(MessageFormat.format("Thank you for cleaning the dishes {0}! The next person on dishes is {1}.", firstName, secondName));
        } else if (text.equals("!Time")) {
            return Optional.of("");
        }
        return Optional.empty();
    }
}
