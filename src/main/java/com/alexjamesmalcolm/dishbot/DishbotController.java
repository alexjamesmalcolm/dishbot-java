package com.alexjamesmalcolm.dishbot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class DishbotController {

    @RequestMapping("/receive-message")
    public void receiveMessage(HttpServletRequest request) throws IOException {
        System.out.println("Message Received");
//        String message = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        String message = "{\"attachments\":[],\"avatar_url\":\"https://i.groupme.com/750x750.jpeg.83f02dee51d24c9386bce40c4da6d445\",\"created_at\":1545193684,\"group_id\":\"46707218\",\"id\":\"154519368481152940\",\"name\":\"Alex Malcolm\",\"sender_id\":\"19742906\",\"sender_type\":\"user\",\"source_guid\":\"7729f2ce786d2f646f0e37744a52d306\",\"system\":false,\"text\":\"test\",\"user_id\":\"19742906\"}";
        System.out.println(message);
        String withTheEndsCutOff = message.substring(1, message.length() - 1);
        System.out.println(withTheEndsCutOff);
        String[] foo = withTheEndsCutOff.split(",");
        Map<String, String> bar = Arrays.stream(foo).collect(Collectors.toMap(x -> {return x.split(":")[0];}, x -> {return x.split(":")[1];}));
        System.out.println(bar);
//        HashMap<String, String> bar = Arrays.stream(foo).collect(
//                Collectors.toMap(x -> {
//            return ((String) x).split(":")[0];
//        }, x -> {
//            return ((String) x).split(":")[1];
//        }));
//        Arrays.stream(foo).collect(Collectors.mapping());
//        withTheEndsCutOff.split("")
    }
}
