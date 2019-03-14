package com.alexjamesmalcolm.dishbot.bean;

import com.alexjamesmalcolm.dishbot.OwnerOnly;
import com.alexjamesmalcolm.dishbot.Properties;
import com.alexjamesmalcolm.dishbot.WheelRepository;
import com.alexjamesmalcolm.dishbot.physical.Account;
import com.alexjamesmalcolm.dishbot.physical.Wheel;
import com.alexjamesmalcolm.groupme.response.Group;
import com.alexjamesmalcolm.groupme.response.Member;
import com.alexjamesmalcolm.groupme.response.Message;
import com.alexjamesmalcolm.groupme.service.GroupMeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;
import static java.util.Locale.US;

@Service
public class Composer {

    private static final String EMPTY_DISH_WHEEL_WARNING = "This group's Dish Wheel is empty, add someone to it by using the !Ids and !Add <USER_ID> commands.";

    @Resource
    private WheelRepository wheelRepo;

    @Resource
    private GroupMeService groupMe;

    @Resource
    private EntityManager em;

    @Resource
    private Properties properties;

    public Optional<String> respond(Account owner, Message message) {
        String text = message.getText().toLowerCase();
        if (text.equals("!dishes")) {
            return dishesDoneCommand(owner, message);
        } else if (text.equals("!time")) {
            return timeLeftCommand(owner, message);
        } else if (text.equals("!ids")) {
            return memberIdsCommand(owner, message);
        } else if (text.startsWith("!add ")) {
            return addUserCommand(owner, message);
        } else if (text.equals("!fines")) {
            return allFinesCommand(owner, message);
        } else if (text.startsWith("!remove ")) {
            return removeUserCommand(owner, message);
        }
        return Optional.empty();
    }

    @OwnerOnly
    private Optional<String> removeUserCommand(Account owner, Message message) {
        Group group = groupMe.getGroup(owner.getAccessToken(), message);
        Optional<Wheel> optionalWheel = wheelRepo.findByGroupId(group.getGroupId());
        if (!optionalWheel.isPresent()) {
            return Optional.of(EMPTY_DISH_WHEEL_WARNING);
        }
        Long userId = message.getUserId();
        Optional<Member> optionalMember = group.queryForMember(userId);
        if (!optionalMember.isPresent()) {
            return Optional.of("Could not find user with id " + userId);
        }
        Wheel wheel = optionalWheel.get();
        wheel.removeMember(userId);
        wheelRepo.save(wheel);
        String name = optionalMember.get().getName();
        return Optional.of("Removed " + name + " from Dish Wheel.");
    }

    private Optional<String> allFinesCommand(Account owner, Message message) {
        Group group = groupMe.getGroup(owner.getAccessToken(), message);
        Optional<Wheel> optionalWheel = wheelRepo.findByGroupId(group.getGroupId());
        if (!optionalWheel.isPresent()) {
            return Optional.of(EMPTY_DISH_WHEEL_WARNING);
        }
        Wheel wheel = optionalWheel.get();
        Map<Long, Double> fines = wheel.getFines();
        String text = "Fines:" + fines.keySet().stream().map(userId -> {
            Optional<Member> optionalMember = group.queryForMember(userId);
            if (!optionalMember.isPresent()) {
                wheel.removeMember(userId);
                return "";
            } else {
                Member member = optionalMember.get();
                String name = member.getName();
                Double fine = fines.get(userId);
                Currency usd = Currency.getInstance("USD");
                NumberFormat format = NumberFormat.getCurrencyInstance(US);
                format.setCurrency(usd);
                return "\n" + name + " -> " + format.format(fine);
            }
        }).collect(Collectors.joining());
        return Optional.of(text);
    }

    @OwnerOnly
    private Optional<String> addUserCommand(Account owner, Message message) {
        Group group = groupMe.getGroup(owner.getAccessToken(), message);
        long userId = parseLong(message.getText().substring(5));
        Optional<Member> potentialMember = group.queryForMember(userId);
        if (potentialMember.isPresent()) {
            Optional<Wheel> potentialWheel = wheelRepo.findByGroupId(group.getGroupId());
            Wheel wheel = potentialWheel.orElse(new Wheel(owner, group.getGroupId()));
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

    private Optional<String> memberIdsCommand(Account owner, Message message) {
        Group group = groupMe.getGroup(owner.getAccessToken(), message);
        List<Member> members = group.getMembers();
        String response = "User IDs:\n" + members.stream().map(member -> member.getName() + " -> " + member.getUserId()).reduce((first, second) -> first + "\n" + second).orElse("");
        return Optional.of(response);
    }

    private Optional<String> timeLeftCommand(Account owner, Message message) {
        long userId = message.getUserId();
        Group group = groupMe.getGroup(owner.getAccessToken(), message);
        Optional<Member> potentialMember = group.queryForMember(userId);
        if (!potentialMember.isPresent()) {
            return Optional.empty();
        }
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

    private Optional<String> dishesDoneCommand(Account owner, Message message) {
        Optional<Wheel> potentialWheel = wheelRepo.findByGroupId(message.getGroupId());
        if (!potentialWheel.isPresent()) {
            return Optional.of(EMPTY_DISH_WHEEL_WARNING);
        }
        Wheel wheel = potentialWheel.get();
        if (!message.getUserId().equals(wheel.getCurrentMemberUserId())) {
            return Optional.empty();
        }
        long currentMemberUserId = wheel.getCurrentMemberUserId();
        wheel.advanceWheel();
        long nextMemberUserId = wheel.getCurrentMemberUserId();
        wheelRepo.save(wheel);
        em.flush();
        em.clear();
        Group group = groupMe.getGroup(owner.getAccessToken(), message);
        String firstName = group.queryForMember(currentMemberUserId).get().getName();
        String secondName = group.queryForMember(nextMemberUserId).get().getName();
        return Optional.of(MessageFormat.format("Thank you for cleaning the dishes {0}! The next person on dishes is {1}.", firstName, secondName));
    }

    public void tryToWarnAll() {
        Set<Wheel> wheels = wheelRepo.findAll();
        wheels.stream().filter(Wheel::needToWarnCurrent).forEach(wheel -> {
            Account owner = wheel.getOwner();
            Group group = groupMe.getGroup(owner.getAccessToken(), wheel.getGroupId());
            long currentId = wheel.getCurrentMemberUserId();
            Optional<Member> optionalMember = group.queryForMember(currentId);
            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();
                String name = member.getName();
                Duration timeLeft = wheel.getDurationUntilFineForCurrent();
                String warning = name + " has " + timeLeft.toHours() + " hours left to do the dishes.";
                String botId = groupMe.getBot(owner.getAccessToken(), group.getGroupId(), properties.getDishbotUrl()).get().getBotId();
                groupMe.sendMessage(warning, botId);
                wheel.currentHasBeenWarned();
            } else {
                wheel.removeMember(currentId);
            }
            wheelRepo.save(wheel);
        });
        em.flush();
        em.clear();
    }

    public void tryToFineAll() {
        Set<Wheel> wheels = wheelRepo.findAll();
        wheels.stream().filter(Wheel::isExpired).forEach(wheel -> {
            Account owner = wheel.getOwner();
            Group group = groupMe.getGroup(owner.getAccessToken(), wheel.getGroupId());
            long currentId = wheel.getCurrentMemberUserId();
            Optional<Member> optionalMember = group.queryForMember(currentId);
            if (optionalMember.isPresent()) {
                Member currentName = optionalMember.get();
                wheel.giveFine(currentName);
                wheel.advanceWheel();
                long nextId = wheel.getCurrentMemberUserId();
                String nextName = group.queryForMember(nextId).get().getName();
                String warning = currentName + " took too long, " + nextName + " is on dishes now.";
                String botId = groupMe.getBot(owner.getAccessToken(), group.getGroupId(), properties.getDishbotUrl()).get().getBotId();
                groupMe.sendMessage(warning, botId);
            } else {
                wheel.removeMember(currentId);
            }
            wheelRepo.save(wheel);
        });
        em.flush();
        em.clear();
    }
}
