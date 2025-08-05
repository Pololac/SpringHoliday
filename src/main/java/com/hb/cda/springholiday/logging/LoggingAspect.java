package com.hb.cda.springholiday.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Around("execution(* com.hb.cda.springholiday.business.*.*(..))")
    public Object logBeforeBusiness(ProceedingJoinPoint joinPoint) throws Throwable {
        long before = System.currentTimeMillis();
        logger.info("Executing method " +joinPoint.getSignature().getName()+" from "+joinPoint.getSignature().getDeclaringTypeName());
        Object result = joinPoint.proceed();
        long after = System.currentTimeMillis();
        logger.info("Finished method " +joinPoint.getSignature().getName()+" from "+joinPoint.getSignature().getDeclaringTypeName() + " in " + (after - before) + " ms");

        return result;
    }
}