package com.alexjamesmalcolm.dishbot.bean;

import com.alexjamesmalcolm.dishbot.WheelRepository;
import com.alexjamesmalcolm.dishbot.groupme.Group;
import com.alexjamesmalcolm.dishbot.groupme.Member;
import com.alexjamesmalcolm.dishbot.groupme.Message;
import com.alexjamesmalcolm.dishbot.physical.Wheel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.Long.parseLong;

@Service
public class Composer {

    public static final String EMPTY_DISH_WHEEL_WARNING = "This group's Dish Wheel is empty, add someone to it using the !Add <USER_ID> and !Ids commands";
    @Resource
    private WheelRepository wheelRepo;

    @Resource
    private GroupMeService groupMe;

    @Resource
    private EntityManager em;

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
        Optional<Member> potentialMember = group.queryForMember(userId);
        if (potentialMember.isPresent()) {
            Optional<Wheel> potentialWheel = wheelRepo.findByGroupId(group.getGroupId());
            Wheel wheel = potentialWheel.orElse(new Wheel(group.getGroupId()));
            wheel.addMember(userId);
            wheelRepo.save(wheel);
            em.flush();
            em.clear();
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
        Optional<Member> potentialMember = group.queryForMember(userId);
        Member member = potentialMember.get();
        String name = member.getName();
        Optional<Wheel> potentialWheel = wheelRepo.findByGroupId(message.getGroupId());
        if (!potentialWheel.isPresent()) {
            return Optional.of(EMPTY_DISH_WHEEL_WARNING);
        }
        Wheel wheel = potentialWheel.get();
        Duration durationUntilFine = wheel.getDurationUntilFineForCurrent();
        long hours = durationUntilFine.toHours();
        return Optional.of(name + " has " + hours + " hours to do the dishes.");
    }

    private Optional<String> dishesDoneCommand(Message message) {
        Optional<Wheel> potentialWheel = wheelRepo.findByGroupId(message.getGroupId());
        if (!potentialWheel.isPresent()) {
            return Optional.of(EMPTY_DISH_WHEEL_WARNING);
        }
        Wheel wheel = potentialWheel.get();
        long currentMemberUserId = wheel.getCurrentMemberUserId();
        wheel.advanceWheel();
        long nextMemberUserId = wheel.getCurrentMemberUserId();
        wheelRepo.save(wheel);
        em.flush();
        em.clear();
        Group group = groupMe.getGroup(message);
        String firstName = group.queryForMember(currentMemberUserId).get().getName();
        String secondName = group.queryForMember(nextMemberUserId).get().getName();
        return Optional.of(MessageFormat.format("Thank you for cleaning the dishes {0}! The next person on dishes is {1}.", firstName, secondName));
    }

    public void tryToWarnAll() {
        Set<Wheel> wheels = wheelRepo.findAll();
        wheels.stream().filter(Wheel::needToWarnCurrent).forEach(wheel -> {
            Group group = groupMe.getGroup(wheel.getGroupId());
            long currentId = wheel.getCurrentMemberUserId();
            Member member = group.queryForMember(currentId).get();
            String name = member.getName();
            Duration timeLeft = wheel.getDurationUntilFineForCurrent();
            String warning = name + " has " + timeLeft.toHours() + " hours left to do the dishes.";
            String botId = groupMe.getBots(group.getGroupId()).get(0).getBotId();
            //TODO Come up with something better than just getting the first bot
            groupMe.sendMessage(warning, botId);
            wheel.currentHasBeenWarned();
            wheelRepo.save(wheel);
        });
    }
}
