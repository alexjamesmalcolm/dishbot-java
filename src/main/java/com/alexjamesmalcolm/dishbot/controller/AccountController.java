package com.alexjamesmalcolm.dishbot.controller;

import com.alexjamesmalcolm.dishbot.AccountRepository;
import com.alexjamesmalcolm.dishbot.physical.Account;
import com.alexjamesmalcolm.groupme.response.Group;
import com.alexjamesmalcolm.groupme.response.Me;
import com.alexjamesmalcolm.groupme.service.GroupMeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Controller
public class AccountController {

    @Resource
    private AccountRepository accountRepo;

    @Resource
    private GroupMeService groupMe;

    private String redirectToGroupMeLogon = "redirect:https://oauth.groupme.com/oauth/authorize?client_id=nYOULhOHkuJcPSUjy3TtnXojCWraPk5GcI25nmkm2aShmQ2z";

    @RequestMapping("/")
    public String displayHomePage() {
        return "home";
    }

    @RequestMapping("/logon")
    public String displaySetupPage() {
        return redirectToGroupMeLogon;
    }

    @RequestMapping("/account")
    public String receiveRedirectFromGroupMe(@RequestParam("access_token") String token) {
        Me user = groupMe.getMe(token);
        Long userId = user.getUserId();
        Optional<Account> optionalAccount = accountRepo.findByUserId(userId);
        Account account = optionalAccount.orElseGet(() -> {
            List<Group> groups = groupMe.getAllGroups(token);
            return accountRepo.save(new Account(token, userId, groups));
        });
        if (!account.getToken().equals(token)) {
            account.updateToken(token);
            accountRepo.save(account);
        }
        return "redirect:/account/" + userId + "?token=" + token;
    }

    @GetMapping("/account/{userId}")
    public String settings(Model model, @PathVariable Long userId, @RequestParam String token) {
        Me user = groupMe.getMe(token);
        Optional<Account> optionalAccount = accountRepo.findByUserId(userId);
        if (!optionalAccount.isPresent()) {
            return "redirect:/account?token=" + token;
        }
        Account account = optionalAccount.get();
        if (!account.getUserId().equals(user.getUserId())) {
            return redirectToGroupMeLogon;
        }
        List<Group> groups = groupMe.getAllGroups(token);
        model.addAttribute("groups", groups);
        account.updateAccount(token, groups);
        account = accountRepo.save(account);
        model.addAttribute("account", account);
        return "account";
    }
}
