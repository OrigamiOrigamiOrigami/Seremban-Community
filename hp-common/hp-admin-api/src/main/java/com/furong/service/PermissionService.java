package com.furong.service;

import com.furong.pojo.dto.PermissionAddDto;
import com.furong.pojo.dto.PermissionQueryDto;
import com.furong.pojo.entity.Permission;
import com.furong.pojo.vo.PermissionVo;
import com.github.pagehelper.Page;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 权限服务接口
 * @author Origami
 * @date 2025/9/13
 */
public interface PermissionService extends IService<Permission> {
    
    /**
     * 添加权限
     * @param permissionAddDto 权限添加DTO
     */
    void insertPermission(PermissionAddDto permissionAddDto);
    
    /**
     * 更新权限
     * @param permission 权限实体
     */
    void updatePermission(Permission permission);
    
    /**
     * 获取权限详情
     * @param id 权限ID
     * @return 权限VO
     */
    PermissionVo getPermissionDetail(Long id);
    
    /**
     * 获取权限列表（包含统计信息）
     * @return 权限VO列表
     */
    List<PermissionVo> getPermissionList();

    /**
     * 分页查询权限列表（包含统计信息）
     * @param permissionQueryDto 查询条件
     * @return 权限分页列表
     */
    Page<PermissionVo> getPermissionListWithPage(PermissionQueryDto permissionQueryDto);
    
    /**
     * 根据权限标识查询权限
     * @param permissionCode 权限标识
     * @return 权限实体
     */
    Permission getByPermissionCode(String permissionCode);
    
    /**
     * 获取管理员的所有权限
     * @param adminId 管理员ID
     * @return 权限列表
     */
    List<Permission> getAdminPermissions(Long adminId);
    
    /**
     * 检查管理员是否拥有指定权限
     * @param adminId 管理员ID
     * @param permissionCode 权限标识
     * @return 是否拥有权限
     */
    boolean hasPermission(Long adminId, String permissionCode);
}
