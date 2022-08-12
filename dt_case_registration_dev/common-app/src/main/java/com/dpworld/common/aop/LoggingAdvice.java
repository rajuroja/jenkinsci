package com.dpworld.common.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAdvice {

	Logger log = LoggerFactory.getLogger(LoggingAdvice.class);
	
	@Pointcut(value="(execution(* com.dpworld.authentication.controller.*.*(..) ) "
			+ "|| execution(* com.dpworld.authentication.service.impl.*.*(..) )"
			+ "|| execution(* com.dpworld.common.utils.*.*(..) )"
			+ "|| execution(* com.dpworld.masterdataapp.controller.*.*(..) )"
			+ "|| execution(* com.dpworld.masterdataapp.service.impl.*.*(..) ))"
			+ "&&  !execution(* com.dpworld.persistence.entity.*.*(..) )"
			)
	public void loggingpointcut() {
		
	}
	
	@Around("loggingpointcut()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),joinPoint.getArgs());
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }

}
