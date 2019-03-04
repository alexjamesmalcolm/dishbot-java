package com.alexjamesmalcolm.dishbot.bean;

import com.alexjamesmalcolm.dishbot.WheelRepository;
import com.alexjamesmalcolm.dishbot.logical.BotMessage;
import com.alexjamesmalcolm.dishbot.physical.Message;
import com.alexjamesmalcolm.dishbot.physical.User;
import com.alexjamesmalcolm.dishbot.physical.Wheel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Optional;

@Service
public class Interpreter {

    private WheelRepository wheelRepo;

    public Optional<BotMessage> respond(Message message) {
        String content = message.getText().toLowerCase();
        boolean isAboutDishes = content.contains("dishes");
        if (!isAboutDishes) {
            return Optional.empty();
        } else {
            boolean isAboutTime = content.contains("time");
            if (isAboutTime) {
                return Optional.of(roommateAskedHowMuchTimeIsLeft(message));
            } else {
                return Optional.of(roommateCompletedDishes(message));
            }
        }
    }

    private BotMessage roommateAskedHowMuchTimeIsLeft(Message message) {
        Wheel wheel = message.getWheel();
        Duration timeLeft = wheel.getDurationUntilFineForCurrentRoommate();
        String name = wheel.getCurrentRoommate().getName();
        String content = name + " has " + timeLeft.toHours() + " hour(s) left.";
        String botId = getBotId(message);
        BotMessage response = new BotMessage(content, botId);
        return response;
    }

    private BotMessage roommateCompletedDishes(Message message) {
        Wheel wheel = message.getWheel();
        User currentRoommate = wheel.getCurrentRoommate();
        String currentName = currentRoommate.getName();
        String nextName = wheel.getRoommateAfter(currentRoommate).getName();
        String text = "Thank you " + currentName + "! You're up on dishes now, " + nextName + ".";
        String botId = getBotId(message);
        wheel.advanceWheel();
        wheelRepo.save(wheel);
        return new BotMessage(text, botId);
    }

    private String getBotId(Message message) {
        return message.getGroup().getBot().getId();
    }

    @Resource
    public void setWheelRepository(WheelRepository wheelRepo) {
        this.wheelRepo = wheelRepo;
    }
}
