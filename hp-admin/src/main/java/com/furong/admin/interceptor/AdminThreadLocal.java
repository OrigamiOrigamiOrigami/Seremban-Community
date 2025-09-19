package com.furong.admin.interceptor;

import com.furong.pojo.vo.AdminInfoVo;

/**
 * @author Origami
 * @date 2025/9/11
 */
public class AdminThreadLocal {

    //准备当前线程的持有类 ThreadLocal
    private static final ThreadLocal<AdminInfoVo> threadLocal = new ThreadLocal<>();


    /**
     * 绑定用户信息到当前线程
     * @param adminInfoVo
     */
    public static void set(AdminInfoVo adminInfoVo) {
        threadLocal.set(adminInfoVo);
    }

    /**
     * 获取当前线程绑定的用户信息
     * @return
     */
    public static AdminInfoVo get() {
        return threadLocal.get();
    }

    /**
     * 移除当前线程绑定的用户信息
     */
    public static void remove() {
        threadLocal.remove();
    }


}
