package com.furong.admin.controller;

import com.furong.admin.interceptor.AdminThreadLocal;
import com.furong.annotation.RequirePermission;
import com.furong.pojo.dto.*;
import com.furong.pojo.vo.AdminInfoVo;
import com.furong.service.AdminService;
import com.furong.pojo.entity.Admin;
import com.furong.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Origami
 * @date 2025/9/9
 */
@RequestMapping("/admin")
@RestController
@Tag(name = "管理员管理相关接口")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/list")
    @Operation(summary = "查询管理员列表")
    @RequirePermission("admin:manage")
    public Result list(@RequestBody AdminQueryDto adminQueryDto) {
        // 查询管理员列表（包含角色信息）
        com.github.pagehelper.Page<AdminInfoVo> adminPage = adminService.getAdminListWithRoles(adminQueryDto);

        // 返回分页数据+总记录数
        return Result.buildSuccess(adminPage.getResult(), adminPage.getTotal());
    }

    @GetMapping("/info")
    @Operation(summary = "查询用户信息")
    public Result find() {
        AdminInfoVo vo = AdminThreadLocal.get();
        if (vo != null) {
            // 查询完整的用户信息（包含角色信息）
            AdminInfoVo detailVo = adminService.getAdminDetailWithRoles(vo.getId());
            if (detailVo != null) {
                return Result.buildSuccess(detailVo);
            }
        }
        return Result.buildSuccess(vo);
    }

    @GetMapping("/findAll")
    @Operation(summary = "查询所有管理员信息")
    @RequirePermission("admin:manage")
    public Result findAll() {
        return Result.buildSuccess(adminService.list());
    }

    @PostMapping("/add")
    @Operation(summary = "添加管理员信息")
    @RequirePermission("admin:manage")
    public Result add(@RequestBody @Validated AdminAddDto adminAddDto){
        adminService.insertAdmin(adminAddDto);
        return Result.buildSuccess();
    }

    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public Result login(@RequestBody @Validated AdminLoginDto adminLoginDto){
        String token = adminService.login(adminLoginDto);
        return Result.buildSuccess(token);
    }

    @PostMapping("/update")
    @Operation(summary = "修改管理员信息")
    @RequirePermission("admin:manage")
    public Result update(@RequestBody Admin admin) {
        boolean result = adminService.updateById(admin);
        return Result.jude(result);
    }

    @GetMapping("/delete")
    @Operation(summary = "删除管理员信息")
    @RequirePermission("admin:manage")
    public Result delete(@RequestParam @Parameter(description = "管理员id") Long id) {
        boolean result = adminService.removeById(id);
        return Result.jude(result);
    }

    @PostMapping("/deleteBatch")
    @Operation(summary = "批量删除管理员信息")
    @RequirePermission("admin:manage")
    public Result deleteBatch(@RequestBody BaseDeleteDto baseDeleteDto) {
        boolean result = adminService.removeByIds(baseDeleteDto.getIds());
        return Result.jude(result);
    }

    @PostMapping("/changePassword")
    @Operation(summary = "修改密码")
    public Result changePassword(@RequestBody @Validated UpdatePasswordDto updatePasswordDto) {
        adminService.changePassword(updatePasswordDto);
        return Result.buildSuccess();
    }
}
