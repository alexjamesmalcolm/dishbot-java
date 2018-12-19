package com.alexjamesmalcolm.dishbot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@RestController
public class DishbotController {

    @RequestMapping("/receive-message")
    public void receiveMessage(HttpServletRequest request) throws IOException {
        String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        String withTheEndsCutOff = json.substring(1, json.length() - 1).replace("\"", "");
        Stream<String> stream = Arrays.stream(withTheEndsCutOff.split(","));
        Map<String, String> map = stream.collect(toMap(x -> x.split(":")[0], x -> x.split(":")[1]));
        Message message = new Message(map);
        System.out.println(message);
    }
}
