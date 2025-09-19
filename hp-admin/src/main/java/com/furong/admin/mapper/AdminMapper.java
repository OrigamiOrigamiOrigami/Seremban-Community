package com.furong.admin.mapper;

import com.furong.pojo.dto.AdminQueryDto;
import com.furong.pojo.entity.Admin;
import com.furong.pojo.vo.AdminInfoVo;
import com.github.pagehelper.Page;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Origami
 * @date 2025/9/9
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 分页查询管理员列表（包含角色信息）
     * @param adminQueryDto 查询条件
     * @return 管理员分页列表
     */
    Page<AdminInfoVo> selectAdminListWithRoles(AdminQueryDto adminQueryDto);

    /**
     * 获取管理员详情（包含角色信息）
     * @param adminId 管理员ID
     * @return 管理员详情
     */
    AdminInfoVo selectAdminDetailWithRoles(@Param("adminId") Long adminId);
}
