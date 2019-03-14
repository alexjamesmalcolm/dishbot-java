package com.alexjamesmalcolm.dishbot.controller;

import com.alexjamesmalcolm.dishbot.AccountRepository;
import com.alexjamesmalcolm.dishbot.physical.Account;
import com.alexjamesmalcolm.groupme.response.Group;
import com.alexjamesmalcolm.groupme.service.GroupMeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Controller
public class AdminController {

    @Resource
    private GroupMeService groupMe;

    @Resource
    private AccountRepository accountRepo;

    @RequestMapping("/")
    public String displayHomePage() {
        return "home";
    }

    @RequestMapping("/setup")
    public String displaySetupPage() {
        return "setup";
    }

    @RequestMapping("/group/{groupId}")
    public String displayManageGroupPage(Model model, @PathVariable Long groupId) {
        Collection<Account> accounts = accountRepo.findAllByGroupId(groupId);
        Optional<Account> optionalAccount = accounts.stream().findAny();
        if (optionalAccount.isPresent()) {
            String accessToken = optionalAccount.get().getAccessToken();
            Group group = groupMe.getGroup(accessToken, groupId);
            model.addAttribute("group", group);
            return "group";
        } else {
            return "redirect:/setup";
        }
    }

    @RequestMapping("/group")
    public String displayAllGroupsPage(Model model) {
        Collection<Account> accounts = accountRepo.findAll();
        Iterable<Group> groups = accounts.stream().map(account -> groupMe.getAllGroups(account.getAccessToken())).flatMap(Collection::stream).distinct().collect(toList());
        model.addAttribute("groups", groups);
        return "group-all";
    }
}
