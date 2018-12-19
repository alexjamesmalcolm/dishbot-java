package com.alexjamesmalcolm.dishbot;

import com.oracle.javafx.jmx.json.JSONException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DishbotController {

    @RequestMapping("/receive-message")
    public void receiveMessage(HttpServletRequest request) throws IOException {
        System.out.println("Message Received");
        String message = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println(message);
        String withTheEndsCutOff = message.substring(1, message.length() - 1);
        System.out.println(withTheEndsCutOff);
    }
}
