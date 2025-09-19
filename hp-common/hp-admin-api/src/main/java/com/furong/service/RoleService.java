package com.furong.service;

import com.furong.pojo.dto.AssignRoleDto;
import com.furong.pojo.dto.RoleAddDto;
import com.furong.pojo.dto.RoleQueryDto;
import com.furong.pojo.entity.Role;
import com.furong.pojo.vo.RoleVo;
import com.github.pagehelper.Page;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 角色服务接口
 * @author Origami
 * @date 2025/9/13
 */
public interface RoleService extends IService<Role> {
    
    /**
     * 添加角色
     * @param roleAddDto 角色添加DTO
     */
    void insertRole(RoleAddDto roleAddDto);
    
    /**
     * 更新角色
     * @param role 角色实体
     */
    void updateRole(Role role);
    
    /**
     * 获取角色详情（包含权限信息）
     * @param id 角色ID
     * @return 角色VO
     */
    RoleVo getRoleDetail(Long id);
    
    /**
     * 获取角色列表（包含统计信息）
     * @return 角色VO列表
     */
    List<RoleVo> getRoleList();

    /**
     * 分页查询角色列表（包含统计信息）
     * @param roleQueryDto 查询条件
     * @return 角色分页列表
     */
    Page<RoleVo> getRoleListWithPage(RoleQueryDto roleQueryDto);
    
    /**
     * 为角色分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    void assignPermissions(Long roleId, List<Long> permissionIds);
    
    /**
     * 为管理员分配角色
     * @param assignRoleDto 分配角色DTO
     */
    void assignRoles(AssignRoleDto assignRoleDto);
    
    /**
     * 获取管理员的角色列表
     * @param adminId 管理员ID
     * @return 角色列表
     */
    List<Role> getAdminRoles(Long adminId);
    
    /**
     * 获取角色的权限ID列表
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> getRolePermissionIds(Long roleId);
}
