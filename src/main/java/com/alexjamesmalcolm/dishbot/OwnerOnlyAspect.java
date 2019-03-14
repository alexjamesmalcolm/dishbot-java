package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.physical.Account;
import com.alexjamesmalcolm.groupme.response.Message;
import com.rollbar.notifier.Rollbar;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Aspect
public class OwnerOnlyAspect {

    @Resource
    private Rollbar logger;

    @Pointcut("@annotation(com.alexjamesmalcolm.dishbot.OwnerOnly)")
    public void ownerOnlyMethods() {
    }

    @Around("ownerOnlyMethods()")
    public Object checkIfOwner(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("METHOD STARTS");
        List<Object> argsList = Arrays.asList(joinPoint.getArgs());
        Account owner = findAccount(argsList);
        Message message = findMessage(argsList);
        if (owner.getUserId().equals(message.getUserId())) {
            return joinPoint.proceed();
        }
        return Optional.empty();
    }

    private Account findAccount(List<Object> argsList) {
        for (Object arg : argsList) {
            try {
                Account account = (Account) arg;
                account.getAccessToken();
                return account;
            } catch (Exception ignored) {
            }
        }
        throw new java.lang.IllegalArgumentException("Could not find Account");
    }

    private Message findMessage(List<Object> argsList) {
        for (Object arg : argsList) {
            try {
                Message message = (Message) arg;
                message.getText();
                return message;
            } catch (Exception ignored) {
            }
        }
        throw new java.lang.IllegalArgumentException("Could not find Message");
    }
}
