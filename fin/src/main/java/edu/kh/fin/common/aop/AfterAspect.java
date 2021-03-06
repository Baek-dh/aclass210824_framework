package edu.kh.fin.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AfterAspect {
	
	private Logger logger = LoggerFactory.getLogger(AfterAspect.class);
	
	// 후처리
	@After("PointcutCollection.controllerPointcut()")
	public void afterLog() {
		logger.info("----------------------------------------------------------------------\n");
	}
	
	
	// 후처리 + 반환값 다루기
	@AfterReturning(pointcut = "PointcutCollection.serviceImplPointcut()", 
					returning = "returnObj")
	public void afterReturningLog(JoinPoint jp, Object returnObj) {
		// 어노테이션에 작성된 returning = "returnObj"와
		// 매개변수에 작성된 변수명이 같으면 
		// 타겟의 메소드 반환값을 얻어와 사용할 수 있음.
		logger.debug("[Return] " + returnObj.toString());
	}
	
	
	// 후처리 + 예외 다루기
	@AfterThrowing(pointcut = "PointcutCollection.serviceImplPointcut()",
				   throwing = "e")	
	public void afterThrowingLog(JoinPoint jp, Exception e) {
		
		String str = "[Exception]";
		
		if(e instanceof NullPointerException) {
			str += "Null 객체를 참조함\n";
			
		}else if(e instanceof IllegalArgumentException) {
			str += "부적절한 값 입력\n";
		
		}else {
			str += "예외 발생\n";
		}
		
		logger.error( str + e.toString() );
	}
	
	
	
	
	
	
	
	
	
	
	
}




