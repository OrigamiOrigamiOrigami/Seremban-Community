package com.furong.admin.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.furong.admin.utils.TokenUtils;
import com.furong.exception.ErrorCode;
import com.furong.pojo.vo.AdminInfoVo;
import com.furong.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * JWT认证拦截器
 * @author Origami
 * @date 2025/9/10
 */
@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的token
        String token = request.getHeader("Authorization");
        
        if (StrUtil.isEmpty(token)) {
            log.warn("请求头中未携带token，请求路径：{}", request.getRequestURI());
            writeErrorResponse(response, ErrorCode.UNAUTHORIZED.getCode(), ErrorCode.UNAUTHORIZED.getMessage());
            return false;
        }

        // 2.去掉Bearer前缀（如果有的话）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            // 3.验证token
            boolean verify = JWTUtil.verify(token, TokenUtils.KEY.getBytes());
            if (!verify) {
                log.warn("token验证失败，token：{}", token);
                writeErrorResponse(response, ErrorCode.TOKEN_INVALID.getCode(), ErrorCode.TOKEN_INVALID.getMessage());
                return false;
            }

            // 4.解析token获取载荷
            JWT jwt = JWTUtil.parseToken(token);
            Object expire = jwt.getPayload("expire");
            
            // 5.检查token是否过期
            if (expire != null) {
                long expireTime = Long.parseLong(expire.toString());
                if (System.currentTimeMillis() > expireTime) {
                    log.warn("token已过期，过期时间：{}", expireTime);
                    writeErrorResponse(response, ErrorCode.TOKEN_EXPIRED.getCode(), ErrorCode.TOKEN_EXPIRED.getMessage());
                    return false;
                }
            }

            Object userId = jwt.getPayload("id");
            Object username = jwt.getPayload("username");
            Object phone = jwt.getPayload("phone");
            Object name = jwt.getPayload("name");
            Object email = jwt.getPayload("email");
            Object image = jwt.getPayload("image");
            Object tenantId = jwt.getPayload("tenantId"); // 获取租户ID

            AdminInfoVo adminInfoVo = new AdminInfoVo();
            adminInfoVo.setId(Long.parseLong(userId.toString()));
            adminInfoVo.setUsername(username.toString());
            adminInfoVo.setPhone(phone.toString());
            adminInfoVo.setName(name.toString());
            adminInfoVo.setEmail(email.toString());
            adminInfoVo.setImage(image.toString());
            // 设置租户ID
            if (tenantId != null) {
                adminInfoVo.setTenantId(Long.parseLong(tenantId.toString()));
            }
            AdminThreadLocal.set(adminInfoVo);

            log.debug("token验证成功，用户ID：{}，用户名：{}", userId, username);
            return true;

        } catch (Exception e) {
            log.error("token解析异常：{}", e.getMessage());
            writeErrorResponse(response, ErrorCode.TOKEN_INVALID.getCode(), ErrorCode.TOKEN_INVALID.getMessage());
            return false;
        }
    }

    /**
     * 写入错误响应
     */
    private void writeErrorResponse(HttpServletResponse response, Integer code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Result<Object> result = Result.buildFail(code, message);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);

        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
