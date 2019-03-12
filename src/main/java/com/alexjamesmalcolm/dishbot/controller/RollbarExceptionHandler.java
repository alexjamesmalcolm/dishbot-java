package com.alexjamesmalcolm.dishbot.controller;

import com.rollbar.notifier.Rollbar;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RollbarExceptionHandler {

    @Resource
    private Rollbar rollbar;

    @ExceptionHandler(Exception.class)
    public String defaultExceptionHandler(Model model, HttpServletRequest request, Exception e) {
        rollbar.error(e);
        model.addAttribute("exception", e);
        model.addAttribute("url", request.getRequestURL());
        return "error";
    }
}
