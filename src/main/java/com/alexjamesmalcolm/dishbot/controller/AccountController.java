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
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Controller
public class AccountController {

    @Resource
    private AccountRepository accountRepo;

    @Resource
    private GroupMeService groupMe;

    @RequestMapping("/")
    public String displayHomePage() {
        return "home";
    }

    @RequestMapping("/setup")
    public String displaySetupPage() {
        return "setup";
    }

    @RequestMapping("/account")
    public String receiveRedirectFromGroupMe(Model model, @RequestParam("access_token") String token) {
        Me user = groupMe.getMe(token);
        Long userId = user.getUserId();
        Optional<Account> optionalAccount = accountRepo.findByUserId(userId);
        Account account = optionalAccount.orElseGet(() -> {
            List<Group> groups = groupMe.getAllGroups(token);
            return accountRepo.save(new Account(token, userId, groups));
        });
        if (!account.getToken().equals(token)) {
            account.updateToken(token);
            account = accountRepo.save(account);
        }
        model.addAttribute("account", account);
        return "redirect:/account/" + userId + "/settings";
    }

    @GetMapping("/account/{userid}/settings")
    public String settings(Model model, @PathVariable Long userId) {
        Optional<Account> optionalAccount = accountRepo.findByUserId(userId);
        Account account = optionalAccount.get();
        model.addAttribute("account", account);
        return "settings";
    }
}
