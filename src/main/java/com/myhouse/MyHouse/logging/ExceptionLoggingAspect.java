package com.myhouse.MyHouse.logging;

import com.myhouse.MyHouse.model.Log;
import com.myhouse.MyHouse.repository.LogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class ExceptionLoggingAspect {

    @Autowired
    private LogRepository logRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @AfterThrowing(pointcut = "execution(* com.myhouse.MyHouse.service..*(..)) || " +
            "execution(* com.myhouse.MyHouse.repository..*(..)) || " +
            "execution(* com.myhouse.MyHouse.controller..*(..)) || " +
            "execution(* com.myhouse.MyHouse.configuration..*(..)) || " +
            "execution(* com.myhouse.MyHouse.security..*(..))",
            throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        logRepository.save(new Log(new Date(), "Error", logger.getName(), exception.getMessage(), Thread.currentThread().getName()));

        logger.error("Exception in method {} of class {}: {}", joinPoint.getSignature().getName(),
                joinPoint.getSignature().getDeclaringTypeName(), exception.getMessage());
    }
}
