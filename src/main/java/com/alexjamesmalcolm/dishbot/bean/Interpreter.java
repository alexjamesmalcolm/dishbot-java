package com.alexjamesmalcolm.dishbot.bean;

import com.alexjamesmalcolm.dishbot.WheelRepository;
import com.alexjamesmalcolm.dishbot.groupme.Group;
import com.alexjamesmalcolm.dishbot.groupme.Member;
import com.alexjamesmalcolm.dishbot.groupme.Message;
import com.alexjamesmalcolm.dishbot.physical.Wheel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static java.lang.Long.parseLong;

@Service
public class Interpreter {

    @Resource
    private WheelRepository wheelRepo;

    @Resource
    private GroupMeService groupMe;

    public Optional<String> respond(Message message) {
        String text = message.getText();
        if (text.equals("!Dishes")) {
            return dishesDoneCommand(message);
        } else if (text.equals("!Time")) {
            return timeLeftCommand(message);
        } else if (text.equals("!Ids")) {
            return memberIdsCommand(message);
        } else if (text.startsWith("!Add ")) {
            return addUserCommand(message);
        }
        return Optional.empty();
    }

    private Optional<String> addUserCommand(Message message) {
        Group group = groupMe.getGroup(message);
        long userId = parseLong(message.getText().substring(5));
        Optional<Member> potentialMember = group.getMember(userId);
        if (potentialMember.isPresent()) {
            Wheel wheel = wheelRepo.findByGroupId(group.getGroupId());
            wheel.addMember(userId);
            wheelRepo.save(wheel);
            String name = potentialMember.get().getName();
            return Optional.of("Added " + name + " to Dish Wheel.");
        } else {
            return Optional.of("Could not find user with id " + userId);
        }
    }

    private Optional<String> memberIdsCommand(Message message) {
        Group group = groupMe.getGroup(message);
        List<Member> members = group.getMembers();
        String response = members.stream().map(member -> member.getName() + " -> " + member.getUserId()).reduce((first, second) -> first + "\n" + second).orElse("");
        return Optional.of(response);
    }

    private Optional<String> timeLeftCommand(Message message) {
        long userId = message.getUserId();
        Group group = groupMe.getGroup(message);
        Optional<Member> potentialMember = group.getMember(userId);
        Member member = potentialMember.get();
        String name = member.getName();
        Wheel wheel = wheelRepo.findByGroupId(message.getGroupId());
        Duration durationUntilFine = wheel.getDurationUntilFineForCurrent();
        long hours = durationUntilFine.toHours();
        return Optional.of(name + " has " + hours + " hours to do the dishes.");
    }

    private Optional<String> dishesDoneCommand(Message message) {
        Wheel wheel = wheelRepo.findByGroupId(message.getGroupId());
        long currentMemberUserId = wheel.getCurrentMemberUserId();
        wheel.advanceWheel();
        long nextMemberUserId = wheel.getCurrentMemberUserId();
        wheelRepo.save(wheel);
        Group group = groupMe.getGroup(message);
        String firstName = group.getMember(currentMemberUserId).get().getName();
        String secondName = group.getMember(nextMemberUserId).get().getName();
        return Optional.of(MessageFormat.format("Thank you for cleaning the dishes {0}! The next person on dishes is {1}.", firstName, secondName));
    }
}
