package com.furong.service;

import com.furong.pojo.dto.AdminAddDto;
import com.furong.pojo.dto.AdminLoginDto;
import com.furong.pojo.dto.AdminQueryDto;
import com.furong.pojo.dto.UpdatePasswordDto;
import com.furong.pojo.entity.Admin;
import com.furong.pojo.vo.AdminInfoVo;
import com.github.pagehelper.Page;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * @author Origami
 * @date 2025/9/9
 */
public interface AdminService extends IService<Admin> {
    /**
     * 添加管理员
     * @param adminAddDto
     */
    void insertAdmin(AdminAddDto adminAddDto);

    /**
     * 登录
     * @param adminLoginDto
     * @return 返回 jwt token
     */
    String login(AdminLoginDto adminLoginDto);

    /**
     * 修改密码
     * @param updatePasswordDto
     */
    void changePassword(UpdatePasswordDto updatePasswordDto);

    /**
     * 分页查询管理员列表（包含角色信息）
     * @param adminQueryDto 查询条件
     * @return 分页结果
     */
    Page<AdminInfoVo> getAdminListWithRoles(AdminQueryDto adminQueryDto);

    /**
     * 获取管理员详情（包含角色信息）
     * @param adminId 管理员ID
     * @return 管理员详情
     */
    AdminInfoVo getAdminDetailWithRoles(Long adminId);
}
