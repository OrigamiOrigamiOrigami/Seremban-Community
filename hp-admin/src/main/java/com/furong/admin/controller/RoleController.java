package com.furong.admin.controller;

import com.furong.annotation.RequirePermission;
import com.furong.pojo.dto.AssignRoleDto;
import com.furong.pojo.dto.BaseDeleteDto;
import com.furong.pojo.dto.RoleAddDto;
import com.furong.pojo.dto.RoleQueryDto;
import com.furong.pojo.entity.Role;
import com.furong.pojo.vo.RoleVo;
import com.furong.result.Result;
import com.furong.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 * @author Origami
 * @date 2025/9/13
 */
@RestController
@RequestMapping("/role")
@Tag(name = "角色管理", description = "角色管理相关接口")
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/list")
    @Operation(summary = "分页查询角色列表")
    @RequirePermission("role:manage")
    public Result list(@RequestBody RoleQueryDto roleQueryDto) {
        Page<RoleVo> page = roleService.getRoleListWithPage(roleQueryDto);
        return Result.buildSuccess(page.getResult(), page.getTotal());
    }



    @GetMapping("/findAll")
    @Operation(summary = "查询所有角色")
    @RequirePermission("role:manage")
    public Result findAll() {
        List<RoleVo> roleList = roleService.getRoleList();
        return Result.buildSuccess(roleList);
    }

    @PostMapping("/save")
    @Operation(summary = "添加角色")
    @RequirePermission("role:manage")
    public Result save(@RequestBody @Validated RoleAddDto roleAddDto) {
        roleService.insertRole(roleAddDto);
        return Result.buildSuccess();
    }

    @PutMapping("/update")
    @Operation(summary = "更新角色")
    public Result update(@RequestBody @Validated Role role) {
        roleService.updateRole(role);
        return Result.buildSuccess();
    }

    @GetMapping("/find/{id}")
    @Operation(summary = "根据ID查询角色详情")
    public Result find(@PathVariable Long id) {
        RoleVo roleVo = roleService.getRoleDetail(id);
        return Result.buildSuccess(roleVo);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除角色")
    public Result delete(@RequestBody @Validated BaseDeleteDto baseDeleteDto) {
        roleService.removeByIds(baseDeleteDto.getIds());
        return Result.buildSuccess();
    }

    @PostMapping("/assignPermissions/{roleId}")
    @Operation(summary = "为角色分配权限")
    public Result assignPermissions(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        roleService.assignPermissions(roleId, permissionIds);
        return Result.buildSuccess();
    }

    @GetMapping("/permissions/{roleId}")
    @Operation(summary = "获取角色的权限ID列表")
    public Result getRolePermissions(@PathVariable Long roleId) {
        List<Long> permissionIds = roleService.getRolePermissionIds(roleId);
        return Result.buildSuccess(permissionIds);
    }

    @PostMapping("/assignRoles")
    @Operation(summary = "为管理员分配角色")
    public Result assignRoles(@RequestBody @Validated AssignRoleDto assignRoleDto) {
        roleService.assignRoles(assignRoleDto);
        return Result.buildSuccess();
    }

    @GetMapping("/adminRoles/{adminId}")
    @Operation(summary = "获取管理员的角色列表")
    public Result getAdminRoles(@PathVariable Long adminId) {
        List<Role> roles = roleService.getAdminRoles(adminId);
        return Result.buildSuccess(roles);
    }
}
