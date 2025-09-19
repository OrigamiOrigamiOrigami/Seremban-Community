package com.furong.admin.mapper;

import com.furong.pojo.dto.PermissionQueryDto;
import com.furong.pojo.entity.Permission;
import com.furong.pojo.vo.PermissionVo;
import com.github.pagehelper.Page;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限Mapper接口
 * @author Origami
 * @date 2025/9/13
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    
    /**
     * 获取权限列表（包含统计信息）
     * @return 权限VO列表
     */
    List<PermissionVo> selectPermissionList();

    /**
     * 分页查询权限列表（包含统计信息和搜索条件）
     * @param permissionQueryDto 查询条件
     * @return 权限分页列表
     */
    Page<PermissionVo> selectPermissionListWithPage(PermissionQueryDto permissionQueryDto);
    
    /**
     * 获取权限详情
     * @param id 权限ID
     * @return 权限VO
     */
    PermissionVo selectPermissionDetail(@Param("id") Long id);
    
    /**
     * 根据权限标识查询权限
     * @param permissionCode 权限标识
     * @return 权限实体
     */
    Permission selectByPermissionCode(@Param("permissionCode") String permissionCode);
    
    /**
     * 获取管理员的所有权限
     * @param adminId 管理员ID
     * @return 权限列表
     */
    List<Permission> selectAdminPermissions(@Param("adminId") Long adminId);
    
    /**
     * 检查管理员是否拥有指定权限
     * @param adminId 管理员ID
     * @param permissionCode 权限标识
     * @return 权限数量（大于0表示拥有权限）
     */
    int checkAdminPermission(@Param("adminId") Long adminId, @Param("permissionCode") String permissionCode);
}
