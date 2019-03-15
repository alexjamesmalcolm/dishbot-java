package com.alexjamesmalcolm.dishbot.controller;

import com.alexjamesmalcolm.dishbot.AccountRepository;
import com.alexjamesmalcolm.dishbot.physical.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class AccountController {

    @Resource
    private AccountRepository accountRepo;

    @GetMapping("/account")
    public String receiveRedirectFromGroupMe(@RequestParam("account_token") String accountToken) {
        Optional<Account> optionalAccount = accountRepo.findByAccessToken(accountToken);
        if (optionalAccount.isPresent()) {
            return "redirect:/settings?accountToken=" + accountToken;
        } else {
            return "redirect:/registration?accountToken=" + accountToken;
        }
    }

    @GetMapping("/settings")
    public String settings(Model model, @RequestParam String accountToken) {
        Optional<Account> optionalAccount = accountRepo.findByAccessToken(accountToken);
        if (!optionalAccount.isPresent()) {
            return "redirect:/registration?accountToken=" + accountToken;
        }
        Account account = optionalAccount.get();
        model.addAttribute("account", account);
        return "settings";
    }

    @GetMapping("/registration")
    public String registration(@RequestParam String accountToken) {
        return "registration";
    }
}
