package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.physical.Account;
import com.alexjamesmalcolm.groupme.response.Message;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
public class DishbotAspect {

    @Pointcut("@annotation(com.alexjamesmalcolm.dishbot.annotation.OwnerOnly)")
    public void annotatedOwnerOnlyMethods() {
    }

    @Around("annotatedOwnerOnlyMethods()")
    public Object checkIfOwner(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("OWNERONLY");
        List<Object> argsList = Arrays.asList(joinPoint.getArgs());
        Account owner = (Account) argsList.get(0);
        Message message = (Message) argsList.get(1);
        if (owner.getUserId().equals(message.getUserId())) {
            return joinPoint.proceed();
        }
        return Optional.empty();
    }

    @Around("@annotation(com.alexjamesmalcolm.dishbot.annotation.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("LOG STARTING");
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        System.out.println(joinPoint.getSignature() + " executed in " + executionTime + "ms");
        return proceed;
    }

}
