package com.edukg.open.config;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD) // 说明该注解只能放在方法上面
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitRequest {
    long time() default 60 * 24; // 限制时间 单位：分钟

    int count() default 200; // 允许请求的次数
}