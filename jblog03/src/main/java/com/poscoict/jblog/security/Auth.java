package com.poscoict.jblog.security;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Auth {
	//어노테이션에 값을 넣을 수가 있다.
	// public String value() default "USER";
	
	//annotaion에 값이 안들어가있어도 디폴트로 설정한다.
	public String role() default "USER";
//	public boolean test() default false;
}

