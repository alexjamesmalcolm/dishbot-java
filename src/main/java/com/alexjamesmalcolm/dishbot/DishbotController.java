package com.alexjamesmalcolm.dishbot;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DishbotController {

    @RequestMapping("/receive-message")
    public void receiveMessage(HttpServletRequest request) {
        System.out.println("Message Received");
        Gson gson = new Gson();
        String json = gson.toJson(request);
        System.out.println(json);
    }
}
