package com.furong.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.furong.admin.mapper.RoleMapper;
import com.furong.exception.CustomerException;
import com.furong.exception.ErrorCode;
import com.furong.pojo.dto.AssignRoleDto;
import com.furong.pojo.dto.RoleAddDto;
import com.furong.pojo.dto.RoleQueryDto;
import com.furong.pojo.entity.Role;
import com.furong.pojo.vo.RoleVo;
import com.furong.service.RoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务实现类
 * @author Origami
 * @date 2025/9/13
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    @Transactional
    public void insertRole(RoleAddDto roleAddDto) {
        // 1. 校验角色名称是否重复
        checkRoleName(roleAddDto.getRoleName());
        
        // 2. 创建角色
        Role role = new Role();
        BeanUtils.copyProperties(roleAddDto, role);
        this.save(role);
        
        // 3. 分配权限
        if (roleAddDto.getPermissionIds() != null && !roleAddDto.getPermissionIds().isEmpty()) {
            assignPermissions(role.getId(), roleAddDto.getPermissionIds());
        }
        
        log.info("添加角色成功：{}", roleAddDto.getRoleName());
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        // 1. 校验角色是否存在
        Role existRole = this.getById(role.getId());
        if (existRole == null) {
            throw new CustomerException(ErrorCode.ROLE_NOT_EXIST);
        }
        
        // 2. 校验角色名称是否重复（排除自己）
        if (!existRole.getRoleName().equals(role.getRoleName())) {
            checkRoleName(role.getRoleName());
        }
        
        // 3. 更新角色
        this.updateById(role);
        
        log.info("更新角色成功：{}", role.getRoleName());
    }

    @Override
    public RoleVo getRoleDetail(Long id) {
        return getMapper().selectRoleDetail(id);
    }

    @Override
    public List<RoleVo> getRoleList() {
        return getMapper().selectRoleList();
    }

    @Override
    public Page<RoleVo> getRoleListWithPage(RoleQueryDto roleQueryDto) {
        // 使用PageHelper分页助手
        PageHelper.startPage(roleQueryDto.getPageNum(), roleQueryDto.getPageSize());
        return getMapper().selectRoleListWithPage(roleQueryDto);
    }

    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        // 1. 删除原有权限关联
        getMapper().deleteRolePermissions(roleId);
        
        // 2. 添加新的权限关联
        if (permissionIds != null && !permissionIds.isEmpty()) {
            getMapper().insertRolePermissions(roleId, permissionIds);
        }
        
        log.info("为角色[{}]分配权限成功，权限数量：{}", roleId, permissionIds != null ? permissionIds.size() : 0);
    }

    @Override
    @Transactional
    public void assignRoles(AssignRoleDto assignRoleDto) {
        // 1. 删除原有角色关联
        getMapper().deleteAdminRoles(assignRoleDto.getAdminId());
        
        // 2. 添加新的角色关联
        if (assignRoleDto.getRoleIds() != null && !assignRoleDto.getRoleIds().isEmpty()) {
            getMapper().insertAdminRoles(assignRoleDto.getAdminId(), assignRoleDto.getRoleIds());
        }
        
        log.info("为管理员[{}]分配角色成功，角色数量：{}", assignRoleDto.getAdminId(), 
                assignRoleDto.getRoleIds() != null ? assignRoleDto.getRoleIds().size() : 0);
    }

    @Override
    public List<Role> getAdminRoles(Long adminId) {
        return getMapper().selectAdminRoles(adminId);
    }

    @Override
    public List<Long> getRolePermissionIds(Long roleId) {
        return getMapper().selectRolePermissionIds(roleId);
    }

    /**
     * 校验角色名称是否重复
     */
    private void checkRoleName(String roleName) {
        if (StrUtil.isEmpty(roleName)) {
            return;
        }
        
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(Role::getRoleName, roleName)
                .eq(Role::getDeleted, 0);
        
        Role existRole = this.getOne(queryWrapper);
        if (existRole != null) {
            throw new CustomerException(ErrorCode.ROLE_NAME_EXIST);
        }
    }
}
