package com.alexjamesmalcolm.dishbot.controller;

import com.alexjamesmalcolm.groupme.service.GroupMeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AdminRestController {

    @Resource
    private GroupMeService groupMe;

    @PostMapping("/group/{groupId}/wheel")
    public void createWheelForGroup(@PathVariable Long groupId, HttpServletResponse response) {
    }

    @PostMapping(value = "/group/{groupId}/wheel", params = "userId")
    public void addUserToWheel(@PathVariable Long groupId, @RequestParam Long userId, HttpServletResponse response) {
    }
}
