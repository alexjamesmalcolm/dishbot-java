package com.alexjamesmalcolm.dishbot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DishbotController {

    @RequestMapping("/receive-message")
    public void receiveMessage(HttpServletRequest request) throws IOException {
        System.out.println("Message Received");
        request.getReader().lines().forEach(line -> {
            System.out.println("This is a seperator");
            System.out.println(line);
        });
//        List<String> list = request.getReader().lines().collect(Collectors.toList());
//        System.out.println(list);
//        String message = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//        System.out.println(message);
    }
}
