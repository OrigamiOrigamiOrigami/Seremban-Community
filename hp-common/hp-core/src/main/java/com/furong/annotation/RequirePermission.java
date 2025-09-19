package com.furong.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限控制注解
 * 用于标记需要权限验证的方法
 * 
 * @author Origami
 * @date 2025/9/13
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    
    /**
     * 权限代码
     * @return 权限代码
     */
    String value();
    
    /**
     * 权限描述（可选）
     * @return 权限描述
     */
    String description() default "";
    
    /**
     * 是否必须拥有该权限（默认为true）
     * @return 是否必须拥有该权限
     */
    boolean required() default true;
}
