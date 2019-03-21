package com.alexjamesmalcolm.dishbot.controller;

import com.alexjamesmalcolm.dishbot.AccountRepository;
import com.alexjamesmalcolm.dishbot.WheelRepository;
import com.alexjamesmalcolm.dishbot.physical.Account;
import com.alexjamesmalcolm.dishbot.physical.Wheel;
import com.alexjamesmalcolm.groupme.response.Group;
import com.alexjamesmalcolm.groupme.response.Me;
import com.alexjamesmalcolm.groupme.response.Member;
import com.alexjamesmalcolm.groupme.service.GroupMeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class AccountController {

    @Resource
    private AccountRepository accountRepo;

    @Resource
    private WheelRepository wheelRepo;

    @Resource
    private GroupMeService groupMe;

    private String redirectToGroupMeLogon = "redirect:https://oauth.groupme.com/oauth/authorize?client_id=nYOULhOHkuJcPSUjy3TtnXojCWraPk5GcI25nmkm2aShmQ2z";

    @RequestMapping("/")
    public String index() {
        return "home";
    }

    @RequestMapping("/logon")
    public String logon() {
        return redirectToGroupMeLogon;
    }

    @RequestMapping("/account")
    public String receiveRedirectFromGroupMe(@RequestParam("access_token") String token) {
        Me user = groupMe.getMe(token);
        Long userId = user.getUserId();
        Optional<Account> optionalAccount = accountRepo.findByUserId(userId);
        Account account = optionalAccount.orElseGet(() -> accountRepo.save(new Account(token, userId)));
        if (!account.getToken().equals(token)) {
            account.updateToken(token);
            accountRepo.save(account);
        }
        return "redirect:/account/" + userId + "?token=" + token;
    }

    @GetMapping("/account/{userId}")
    public String account(Model model, @PathVariable Long userId, @RequestParam String token) {
        Me user = groupMe.getMe(token);
        Optional<Account> optionalAccount = accountRepo.findByUserId(userId);
        if (!optionalAccount.isPresent()) {
            return "redirect:/account?token=" + token;
        }
        Account account = optionalAccount.get();
        if (!account.getUserId().equals(user.getUserId())) {
            return redirectToGroupMeLogon;
        }
        account.updateAccount(token);
        account = accountRepo.save(account);
        model.addAttribute("token", token);
        model.addAttribute("account", account);
        return "account";
    }

    @RequestMapping("/account/{userId}/group/{groupId}/wheel")
    public String wheel(Model model, @PathVariable Long userId, @PathVariable Long groupId, @RequestParam String token) {
        Optional<Account> optionalAccount = accountRepo.findByUserId(userId);
        if (!optionalAccount.isPresent()) {
            return "redirect:/account?token=" + token;
        }
        Account account = optionalAccount.get();
        Optional<Wheel> optionalWheel = account.getWheel(groupId);
        Wheel wheel = optionalWheel.orElseGet(() -> wheelRepo.save(new Wheel(account, groupId)));
        model.addAttribute("token", token);
        model.addAttribute("account", account);
        model.addAttribute("wheel", wheel);
        return "wheel";
    }

    @RequestMapping("/account/{accountUserId}/group/{groupId}/wheel/{userId}/add")
    public String addUserToWheel(@PathVariable Long accountUserId, @PathVariable Long groupId, @PathVariable Long userId, @RequestParam String token) {
        Optional<Account> optionalAccount = accountRepo.findByUserId(accountUserId);
        if (!optionalAccount.isPresent()) {
            return "redirect:/account?token=" + token;
        }
        Account account = optionalAccount.get();
        Optional<Wheel> optionalWheel = account.getWheel(groupId);
        Wheel wheel = optionalWheel.orElseGet(() -> wheelRepo.save(new Wheel(account, groupId)));
        Group group = wheel.getGroup();
        Optional<Member> optionalMember = group.queryForMember(userId);
        String finalDestination = "/account/" + accountUserId + "/group/" + groupId + "/wheel?token=" + token;
        if (!optionalMember.isPresent()) {
            return finalDestination;
        }
        Member member = optionalMember.get();
        wheel.addMember(member);
        wheelRepo.save(wheel);
        return finalDestination;
    }

    @RequestMapping("/account/{accountUserId}/group/{groupId}/wheel/{userId}/remove")
    public String removeUserFromWheel(@PathVariable Long accountUserId, @PathVariable Long groupId, @PathVariable Long userId, @RequestParam String token) {
        Optional<Account> optionalAccount = accountRepo.findByUserId(accountUserId);
        if (!optionalAccount.isPresent()) {
            return "redirect:/account?token=" + token;
        }
        Account account = optionalAccount.get();
        Optional<Wheel> optionalWheel = account.getWheel(groupId);
        Wheel wheel = optionalWheel.orElseGet(() -> wheelRepo.save(new Wheel(account, groupId)));
        Group group = wheel.getGroup();
        Optional<Member> optionalMember = group.queryForMember(userId);
        String finalDestination = "/account/" + accountUserId + "/group/" + groupId + "/wheel?token=" + token;
        if (!optionalMember.isPresent()) {
            return finalDestination;
        }
        Member member = optionalMember.get();
        wheel.removeMember(member);
        wheelRepo.save(wheel);
        return finalDestination;
    }
}
