package com.furong.admin.controller;

import com.furong.annotation.RequirePermission;
import com.furong.pojo.dto.BaseDeleteDto;
import com.furong.pojo.dto.PermissionAddDto;
import com.furong.pojo.dto.PermissionQueryDto;
import com.furong.pojo.entity.Permission;
import com.furong.pojo.vo.PermissionVo;
import com.furong.result.Result;
import com.furong.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 * @author Origami
 * @date 2025/9/13
 */
@RestController
@RequestMapping("/permission")
@Tag(name = "权限管理", description = "权限管理相关接口")
@Slf4j
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping("/list")
    @Operation(summary = "分页查询权限列表")
    @RequirePermission("permission:manage")
    public Result list(@RequestBody PermissionQueryDto permissionQueryDto) {
        Page<PermissionVo> page = permissionService.getPermissionListWithPage(permissionQueryDto);
        return Result.buildSuccess(page.getResult(), page.getTotal());
    }

    @GetMapping("/findAll")
    @Operation(summary = "查询所有权限")
    @RequirePermission("permission:manage")
    public Result findAll() {
        List<PermissionVo> permissionList = permissionService.getPermissionList();
        return Result.buildSuccess(permissionList);
    }

    @PostMapping("/save")
    @Operation(summary = "添加权限")
    @RequirePermission("permission:manage")
    public Result save(@RequestBody @Validated PermissionAddDto permissionAddDto) {
        permissionService.insertPermission(permissionAddDto);
        return Result.buildSuccess();
    }

    @PutMapping("/update")
    @Operation(summary = "更新权限")
    public Result update(@RequestBody @Validated Permission permission) {
        permissionService.updatePermission(permission);
        return Result.buildSuccess();
    }

    @GetMapping("/find/{id}")
    @Operation(summary = "根据ID查询权限详情")
    public Result find(@PathVariable Long id) {
        PermissionVo permissionVo = permissionService.getPermissionDetail(id);
        return Result.buildSuccess(permissionVo);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除权限")
    public Result delete(@RequestBody @Validated BaseDeleteDto baseDeleteDto) {
        permissionService.removeByIds(baseDeleteDto.getIds());
        return Result.buildSuccess();
    }

    @GetMapping("/adminPermissions/{adminId}")
    @Operation(summary = "获取管理员的权限列表")
    public Result getAdminPermissions(@PathVariable Long adminId) {
        List<Permission> permissions = permissionService.getAdminPermissions(adminId);
        return Result.buildSuccess(permissions);
    }

    @GetMapping("/checkPermission/{adminId}/{permissionCode}")
    @Operation(summary = "检查管理员是否拥有指定权限")
    public Result checkPermission(@PathVariable Long adminId, @PathVariable String permissionCode) {
        boolean hasPermission = permissionService.hasPermission(adminId, permissionCode);
        return Result.buildSuccess(hasPermission);
    }
}
