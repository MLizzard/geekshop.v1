package com.study.geekshop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before(
            "execution(* com.study.geekshop..*(..))"
    )
    public void logBefore(JoinPoint joinPoint) {
        if (logger.isInfoEnabled()) {
            logger.info("CALL: {}", joinPoint.getSignature().toShortString());
        }
    }

    @AfterReturning(
            pointcut = "execution(* com.study.geekshop..*(..))", returning = "result"
    )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        if (logger.isInfoEnabled()) {
            String resultString;

            if (result == null) {
                resultString = "null";
            } else if (isSimpleType(result)) {
                resultString = result.toString();
            } else {
                resultString = "[object of type " + result.getClass().getSimpleName() + "]";
            }

            logger.info("EXECUTED: {} RETURNED: {}", joinPoint.getSignature().toShortString(), resultString);
        }
    }

    @AfterThrowing(
            pointcut = "execution(* com.study.geekshop..*(..))", throwing = "error"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        if (logger.isErrorEnabled()) {
            logger.error("OCCURED: {} REASON: {}",
                    joinPoint.getSignature().toShortString(), error.getMessage());
        }
    }

    private boolean isSimpleType(Object obj) {
        return obj instanceof String
                || obj instanceof Number
                || obj instanceof Boolean
                || obj.getClass().isPrimitive();
    }
}
