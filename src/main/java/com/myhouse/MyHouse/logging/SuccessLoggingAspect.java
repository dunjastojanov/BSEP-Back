package com.myhouse.MyHouse.logging;

import com.myhouse.MyHouse.model.Log;
import com.myhouse.MyHouse.repository.LogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class SuccessLoggingAspect {

    @Autowired
    private LogRepository logRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @AfterReturning(pointcut = "@annotation(LogSuccess)")
    public void logAfterExecution(JoinPoint joinPoint) {
        String message = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(LogSuccess.class).message();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        logRepository.save(new Log(
                new Date(),
                "INFO",
                logger.getName(),
                String.format("Execution of method %s in class %s completed successfully. Message: %s",
                        methodName, className, message),
                Thread.currentThread().getName()));

        logger.info("Execution of method {} in class {} completed successfully. Message: {}",
                methodName,
                className,
                message);
    }

}
