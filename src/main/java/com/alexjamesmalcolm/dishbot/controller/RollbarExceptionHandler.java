package com.alexjamesmalcolm.dishbot.controller;

import com.rollbar.notifier.Rollbar;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class RollbarExceptionHandler {

    @Resource
    private Rollbar rollbar;

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultExceptionHandler(HttpServletRequest request, Exception e) {
        ModelAndView mav = new ModelAndView();
        rollbar.error(e);
        mav.addObject("exception", e);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName("error");
        mav.setStatus(INTERNAL_SERVER_ERROR);
        return mav;
    }
}
