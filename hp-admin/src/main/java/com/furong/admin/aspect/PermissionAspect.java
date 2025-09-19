package com.furong.admin.aspect;

import com.furong.admin.interceptor.AdminThreadLocal;
import com.furong.annotation.RequirePermission;
import com.furong.exception.CustomerException;
import com.furong.exception.ErrorCode;
import com.furong.pojo.vo.AdminInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 权限控制切面
 * 基于注解的权限验证
 * 
 * @author Origami
 * @date 2025/9/13
 */
@Aspect
@Component
@Slf4j
public class PermissionAspect {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 定义切点：所有标注了@RequirePermission注解的方法
     */
    @Pointcut("@annotation(com.furong.annotation.RequirePermission)")
    public void permissionPointcut() {}

    /**
     * 环绕通知：权限验证
     */
    @Around("permissionPointcut()")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // 获取当前登录用户信息
            AdminInfoVo currentAdmin = AdminThreadLocal.get();
            if (currentAdmin == null) {
                log.warn("用户未登录，拒绝访问");
                throw new CustomerException(ErrorCode.USER_NOT_LOGIN);
            }

            // 获取方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            
            // 获取权限注解
            RequirePermission requirePermission = method.getAnnotation(RequirePermission.class);
            if (requirePermission == null) {
                // 如果方法上没有注解，检查类上是否有注解
                requirePermission = method.getDeclaringClass().getAnnotation(RequirePermission.class);
            }
            
            if (requirePermission != null) {
                String permissionCode = requirePermission.value();
                boolean required = requirePermission.required();
                
                log.debug("检查用户[{}]是否拥有权限[{}]", currentAdmin.getUsername(), permissionCode);

                // 从Redis获取用户权限
                String permissionKey = "admin:permissions:" + currentAdmin.getTenantId() + ":" + currentAdmin.getId();
                List<String> userPermissions = (List<String>) redisTemplate.opsForValue().get(permissionKey);

                // 检查权限
                boolean hasPermission = userPermissions != null && userPermissions.contains(permissionCode);

                if (required && !hasPermission) {
                    log.warn("用户[{}]没有权限[{}]，拒绝访问方法[{}]",
                            currentAdmin.getUsername(), permissionCode, method.getName());
                    throw new CustomerException(ErrorCode.NO_PERMISSION);
                }

                if (hasPermission) {
                    log.debug("用户[{}]拥有权限[{}]，允许访问", currentAdmin.getUsername(), permissionCode);
                } else {
                    log.debug("用户[{}]没有权限[{}]，但该权限不是必需的，允许访问",
                            currentAdmin.getUsername(), permissionCode);
                }
            }
            
            // 执行目标方法
            return joinPoint.proceed();
            
        } catch (CustomerException e) {
            // 重新抛出业务异常
            throw e;
        } catch (Exception e) {
            log.error("权限检查过程中发生异常", e);
            throw new CustomerException(ErrorCode.SYSTEM_ERROR);
        }
    }
}
