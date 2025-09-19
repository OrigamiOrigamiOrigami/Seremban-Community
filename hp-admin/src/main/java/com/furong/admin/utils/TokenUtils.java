package com.furong.admin.utils;

import cn.hutool.jwt.JWTUtil;

import java.util.Map;

/**
 * @author Origami
 * @date 2025/9/10
 */
public class TokenUtils {

    public static final String KEY = "f3340f3041e67372e731872bb01a144f12e649eac4d40a319c036e29218918ce87abdeasfaqwfwgrehrh";

    /**
     * 创建JWT令牌
     * @param payload
     * @return
     */
    public static String createToken(Map<String, Object> payload){
        return JWTUtil.createToken(payload, KEY.getBytes());
    }

}
