package com.furong.admin.mapper;

import com.furong.pojo.dto.RoleQueryDto;
import com.furong.pojo.entity.Role;
import com.furong.pojo.vo.RoleVo;
import com.github.pagehelper.Page;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 * @author Origami
 * @date 2025/9/13
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    
    /**
     * 获取角色列表（包含统计信息）
     * @return 角色VO列表
     */
    List<RoleVo> selectRoleList();

    /**
     * 分页查询角色列表（包含统计信息和搜索条件）
     * @param roleQueryDto 查询条件
     * @return 角色分页列表
     */
    Page<RoleVo> selectRoleListWithPage(RoleQueryDto roleQueryDto);
    
    /**
     * 获取角色详情（包含权限信息）
     * @param id 角色ID
     * @return 角色VO
     */
    RoleVo selectRoleDetail(@Param("id") Long id);
    
    /**
     * 获取管理员的角色列表
     * @param adminId 管理员ID
     * @return 角色列表
     */
    List<Role> selectAdminRoles(@Param("adminId") Long adminId);
    
    /**
     * 获取角色的权限ID列表
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> selectRolePermissionIds(@Param("roleId") Long roleId);
    
    /**
     * 删除角色的所有权限关联
     * @param roleId 角色ID
     */
    void deleteRolePermissions(@Param("roleId") Long roleId);
    
    /**
     * 批量插入角色权限关联
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    void insertRolePermissions(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);
    
    /**
     * 删除管理员的所有角色关联
     * @param adminId 管理员ID
     */
    void deleteAdminRoles(@Param("adminId") Long adminId);
    
    /**
     * 批量插入管理员角色关联
     * @param adminId 管理员ID
     * @param roleIds 角色ID列表
     */
    void insertAdminRoles(@Param("adminId") Long adminId, @Param("roleIds") List<Long> roleIds);
}
