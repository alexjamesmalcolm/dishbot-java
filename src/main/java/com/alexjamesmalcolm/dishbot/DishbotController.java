package com.alexjamesmalcolm.dishbot;

import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DishbotController {

    @RequestMapping("/receive-message")
    public void receiveMessage(HttpRequest request) {
        System.out.println("Message Received");
        System.out.println(request.toString());
    }
}
