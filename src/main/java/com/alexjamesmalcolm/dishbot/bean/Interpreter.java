package com.alexjamesmalcolm.dishbot.bean;

import com.alexjamesmalcolm.dishbot.WheelRepository;
import com.alexjamesmalcolm.dishbot.groupme.Group;
import com.alexjamesmalcolm.dishbot.groupme.Member;
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

    @Resource
    private GroupMeService groupMe;

    public Optional<String> respond(Message message) {
        String text = message.getText();
        if (text.equals("!Dishes")) {
            Wheel wheel = wheelRepo.findByGroupId(message.getGroupId());
            long currentMemberUserId = wheel.getCurrentMemberUserId();
            wheel.advanceWheel();
            long nextMemberUserId = wheel.getCurrentMemberUserId();
            wheelRepo.save(wheel);
            Group group = groupMe.getGroup(message);
            String firstName = group.getMember(currentMemberUserId).get().getName();
            String secondName = group.getMember(nextMemberUserId).get().getName();
            return Optional.of(MessageFormat.format("Thank you for cleaning the dishes {0}! The next person on dishes is {1}.", firstName, secondName));
        } else if (text.equals("!Time")) {
            long userId = message.getUserId();
            Group group = groupMe.getGroup(message);
            Optional<Member> potentialMember = group.getMember(userId);
            Member member = potentialMember.get();
            String name = member.getName();
            return Optional.of(name + " has 48 hours to do the dishes.");
        }
        return Optional.empty();
    }
}
