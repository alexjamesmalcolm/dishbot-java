package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.exception.NotFoundException;
import com.alexjamesmalcolm.dishbot.physical.Group;
import com.alexjamesmalcolm.dishbot.physical.User;
import com.alexjamesmalcolm.dishbot.physical.Wheel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.RESET_CONTENT;

@RestController
public class AdminRestController {

    @Resource
    private GroupRepository groupRepo;

    @Resource
    private UserRepository userRepo;

    @PostMapping("/group/{groupId}/wheel")
    public void createWheelForGroup(@PathVariable Long groupId, HttpServletResponse response) {
        Group group = findGroup(groupId);
        Wheel wheel = group.getWheel();
        if (wheel == null) {
            group.createWheel();
            groupRepo.save(group);
            response.setStatus(RESET_CONTENT.value());
        } else {
            response.setStatus(OK.value());
        }
    }

    @PostMapping("/group/{groupId}/wheel")
    public void addUserToWheel(@PathVariable Long groupId, @RequestParam Long userId, HttpServletResponse response) {
        Group group = findGroup(groupId);
        User user = findUser(userId);
        Wheel wheel = group.getWheel();
        wheel.addRoommate(user);
        groupRepo.save(group);
    }

    private Group findGroup(Long groupId) {
        return groupRepo.findById(groupId).orElseThrow(() -> new NotFoundException("Group not found."));
    }

    private User findUser(Long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new NotFoundException("User not found."));
    }
}
