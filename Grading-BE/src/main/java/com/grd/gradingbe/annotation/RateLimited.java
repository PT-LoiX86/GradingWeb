package com.grd.gradingbe.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {
    int maxRequests() default 10;
    int timeWindowMinutes() default 1;
    String message() default "Too many requests. Please try again later.";
}
