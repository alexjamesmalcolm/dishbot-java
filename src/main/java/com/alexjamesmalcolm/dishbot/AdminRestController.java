package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.exception.NotFoundException;
import com.alexjamesmalcolm.dishbot.physical.Group;
import com.alexjamesmalcolm.dishbot.physical.Wheel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.RESET_CONTENT;

@RestController
public class AdminRestController {

    @Resource
    private GroupRepository groupRepo;

    @PostMapping("/group/{groupId}/wheel")
    public void createWheelForGroup(@PathVariable Long groupId, HttpServletResponse response) {
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new NotFoundException("Group not found."));
        Wheel wheel = group.getWheel();
        if (wheel == null) {
            group.createWheel();
            groupRepo.save(group);
            response.setStatus(RESET_CONTENT.value());
        } else {
            response.setStatus(OK.value());
        }
    }
}
