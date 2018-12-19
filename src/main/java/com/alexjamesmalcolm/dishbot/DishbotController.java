package com.alexjamesmalcolm.dishbot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class DishbotController {

    @Resource
    private MessageRepository messageRepo;

    @RequestMapping("/")
    public String displayIndex(Model model) {
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "index";
    }
}
