package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.physical.Group;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class AdminController {

    @Resource
    private GroupRepository groupRepo;

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
        Group group = groupRepo.findById(groupId).get();
        model.addAttribute("group", group);
        return "group";
    }

    @RequestMapping("/group")
    public String displayAllGroupsPage(Model model) {
        Iterable<Group> groups = groupRepo.findAll();
        model.addAttribute("groups", groups);
        return "group-all";
    }
}
