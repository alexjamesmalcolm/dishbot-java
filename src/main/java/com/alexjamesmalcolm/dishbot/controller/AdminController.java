package com.alexjamesmalcolm.dishbot.controller;

import com.alexjamesmalcolm.dishbot.bean.GroupMeService;
import com.alexjamesmalcolm.dishbot.groupme.Group;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class AdminController {

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

    @RequestMapping("/group/{groupId}")
    public String displayManageGroupPage(Model model, @PathVariable Long groupId) {
        Group group = groupMe.getGroup(groupId);
        model.addAttribute("group", group);
        return "group";
    }

    @RequestMapping("/group")
    public String displayAllGroupsPage(Model model) {
        Iterable<Group> groups = groupMe.getAllGroups();
        model.addAttribute("groups", groups);
        return "group-all";
    }
}
