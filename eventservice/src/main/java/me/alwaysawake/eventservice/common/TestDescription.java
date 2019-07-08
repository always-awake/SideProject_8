package me.alwaysawake.eventservice.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE) // 애노테이션을 붙인 코드를 얼마나 오래 유지할 것인가(얼마나 오래 가져갈 것인가), 기본값으로는 CLASS
public @interface TestDescription {

    String value();
}
