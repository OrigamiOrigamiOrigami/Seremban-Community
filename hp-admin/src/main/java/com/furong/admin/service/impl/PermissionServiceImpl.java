package com.furong.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.furong.admin.mapper.PermissionMapper;
import com.furong.exception.CustomerException;
import com.furong.exception.ErrorCode;
import com.furong.pojo.dto.PermissionAddDto;
import com.furong.pojo.dto.PermissionQueryDto;
import com.furong.pojo.entity.Permission;
import com.furong.pojo.vo.PermissionVo;
import com.furong.service.PermissionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限服务实现类
 * @author Origami
 * @date 2025/9/13
 */
@Service
@Slf4j
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Override
    public void insertPermission(PermissionAddDto permissionAddDto) {
        // 1. 校验权限标识是否重复
        checkPermissionCode(permissionAddDto.getPermissionCode());
        
        // 2. 创建权限
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionAddDto, permission);
        this.save(permission);
        
        log.info("添加权限成功：{}", permissionAddDto.getPermissionName());
    }

    @Override
    public void updatePermission(Permission permission) {
        // 1. 校验权限是否存在
        Permission existPermission = this.getById(permission.getId());
        if (existPermission == null) {
            throw new CustomerException(ErrorCode.PERMISSION_NOT_EXIST);
        }
        
        // 2. 校验权限标识是否重复（排除自己）
        if (!existPermission.getPermissionCode().equals(permission.getPermissionCode())) {
            checkPermissionCode(permission.getPermissionCode());
        }
        
        // 3. 更新权限
        this.updateById(permission);
        
        log.info("更新权限成功：{}", permission.getPermissionName());
    }

    @Override
    public PermissionVo getPermissionDetail(Long id) {
        return getMapper().selectPermissionDetail(id);
    }

    @Override
    public List<PermissionVo> getPermissionList() {
        return getMapper().selectPermissionList();
    }

    @Override
    public Page<PermissionVo> getPermissionListWithPage(PermissionQueryDto permissionQueryDto) {
        // 使用PageHelper分页助手
        PageHelper.startPage(permissionQueryDto.getPageNum(), permissionQueryDto.getPageSize());
        return getMapper().selectPermissionListWithPage(permissionQueryDto);
    }

    @Override
    public Permission getByPermissionCode(String permissionCode) {
        return getMapper().selectByPermissionCode(permissionCode);
    }

    @Override
    public List<Permission> getAdminPermissions(Long adminId) {
        return getMapper().selectAdminPermissions(adminId);
    }

    @Override
    public boolean hasPermission(Long adminId, String permissionCode) {
        int count = getMapper().checkAdminPermission(adminId, permissionCode);
        return count > 0;
    }

    /**
     * 校验权限标识是否重复
     */
    private void checkPermissionCode(String permissionCode) {
        if (StrUtil.isEmpty(permissionCode)) {
            return;
        }
        
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(Permission::getPermissionCode, permissionCode)
                .eq(Permission::getDeleted, 0);
        
        Permission existPermission = this.getOne(queryWrapper);
        if (existPermission != null) {
            throw new CustomerException(ErrorCode.PERMISSION_CODE_EXIST);
        }
    }
}
