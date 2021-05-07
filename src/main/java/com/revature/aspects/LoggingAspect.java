package com.revature.aspects;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
	
	private Logger logger;
	
	@Around("everything()")
	public Object log(ProceedingJoinPoint pjp) throws Throwable{
		Object result = null;
		logger = LogManager.getLogger(pjp.getTarget().getClass());
		logger.trace("Called Method with signature " + pjp.getSignature()); 
		logger.trace("With arguments " + Arrays.toString(pjp.getArgs()));
		try {
			result = pjp.proceed();
		}catch(Throwable e) {
			logger.error("Method threw excpetion " + e);
			for(StackTraceElement el : e.getStackTrace()) {
				logger.warn(el);
			}
			if(e.getCause() != null) {
				Throwable t = e.getCause();
				logger.error("Method threw wrapped Exception " + t);
				for(StackTraceElement el : t.getStackTrace()) {
					logger.warn(el);
				}
			}
			throw e;
		}
		logger.trace("Method return with result " + result);
		return result;
	}

	
	//Hook - only exists as a target for an annotation
	@Pointcut("execution( * com.revature..*(..) )")
	private void everything() {} //empty method for hook
	
}
