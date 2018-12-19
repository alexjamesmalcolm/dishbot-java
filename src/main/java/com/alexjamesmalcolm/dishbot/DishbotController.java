package com.alexjamesmalcolm.dishbot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
public class DishbotController {

    @RequestMapping("/receive-message")
    public void receiveMessage(HttpServletRequest request) throws IOException, ServletException {
        System.out.println("Message Received");
        StringBuilder sb = new StringBuilder();
        sb.append(" [");
        sb.append("\n\t ContextPath: ").append(request.getContextPath());
        sb.append("\n\t LocalAddr: ").append(request.getLocalAddr());
        sb.append("\n\t LocalName: ").append(request.getLocalName());
        sb.append("\n\t LocalPort: ").append(request.getLocalPort());
        sb.append("\n\t PathInfo: ").append(request.getPathInfo());
        sb.append("\n\t PathTranslated: ").append(request.getPathTranslated());
        sb.append("\n\t Protocol: ").append(request.getProtocol());
        sb.append("\n\t RemoteAddr: ").append(request.getRemoteAddr());
        sb.append("\n\t RemoteHost: ").append(request.getRemoteHost());
        sb.append("\n\t RemotePort: ").append(request.getRemotePort());
        sb.append("\n\t RequestURI: ").append(request.getRequestURI());
        sb.append("\n\t RequestURL: ").append(request.getRequestURL());
        sb.append("\n\t Scheme: " ).append(request.getScheme());
        sb.append("\n\t ServerName: ").append(request.getServerName());
        sb.append("\n\t ServerPort: ").append(request.getServerPort());
        sb.append("\n\t ServletPath: ").append(request.getServletPath());
        List<Part> parts = (List<Part>) request.getParts();
        for (int i = 0; i < parts.size(); i++) {
            sb.append("\n\t Part(" + i + "): ").append(parts.get(i));
        }
        sb.append("\n]");
        System.out.println(sb);
    }
}
