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
public class DishbotAspect {

    @Resource
    private Rollbar logger;

    @Pointcut("@annotation(OwnerOnly)")
    public void annotatedOwnerOnlyMethods() {
    }

    @Pointcut("execution(* *..*(com.alexjamesmalcolm.dishbot.physical.Account,..))")
    public void firstParameterAccount() {
    }

    @Pointcut("execution(* *..*(*,com.alexjamesmalcolm.groupme.response.Message,..))")
    public void secondParameterMessage() {
    }

    @Around("annotatedOwnerOnlyMethods() && firstParameterAccount() && secondParameterMessage()")
    public Object checkIfOwner(ProceedingJoinPoint joinPoint) throws Throwable {
        List<Object> argsList = Arrays.asList(joinPoint.getArgs());
        Account owner = (Account) argsList.get(0);
        Message message = (Message) argsList.get(1);
        if (owner.getUserId().equals(message.getUserId())) {
            return joinPoint.proceed();
        }
        return Optional.empty();
    }

}
