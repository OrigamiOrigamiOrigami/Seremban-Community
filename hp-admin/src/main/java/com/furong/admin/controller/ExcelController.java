package com.furong.admin.controller;

import com.alibaba.excel.EasyExcel;
import com.furong.admin.listener.PermissionDataListener;
import com.furong.pojo.dto.AdminQueryDto;
import com.furong.pojo.dto.PermissionQueryDto;
import com.furong.pojo.dto.RoleQueryDto;
import com.furong.pojo.excel.AdminExcelData;
import com.furong.pojo.excel.PermissionExcelData;
import com.furong.pojo.excel.RoleExcelData;
import com.furong.pojo.vo.AdminInfoVo;
import com.furong.pojo.vo.PermissionVo;
import com.furong.pojo.vo.RoleVo;
import com.furong.result.Result;
import com.furong.service.AdminService;
import com.furong.service.PermissionService;
import com.furong.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Excel导出导入控制器
 * @author Origami
 * @date 2025/9/13
 */
@RestController
@RequestMapping("/excel")
@Tag(name = "Excel导出导入", description = "Excel导出导入相关接口")
@Slf4j
public class ExcelController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 导出管理员数据
     */
    @PostMapping("/export/admin")
    @Operation(summary = "导出管理员数据")
    public void exportAdmin(@RequestBody AdminQueryDto adminQueryDto, HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("管理员数据", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 查询所有数据（使用大分页参数获取所有数据）
            // 注意：不能设置为null，因为PageHelper.startPage()不支持null值
            adminQueryDto.setPageNum(1);
            adminQueryDto.setPageSize(Integer.MAX_VALUE); // 使用最大值确保获取所有数据
            List<AdminInfoVo> adminList = adminService.getAdminListWithRoles(adminQueryDto).getResult();

            // 转换为Excel数据格式
            List<AdminExcelData> excelDataList = adminList.stream().map(admin -> {
                AdminExcelData excelData = new AdminExcelData();
                BeanUtils.copyProperties(admin, excelData);
                return excelData;
            }).collect(Collectors.toList());

            // 写入Excel
            EasyExcel.write(response.getOutputStream(), AdminExcelData.class)
                    .sheet("管理员数据")
                    .doWrite(excelDataList);

            log.info("导出管理员数据成功，共{}条记录", excelDataList.size());
        } catch (Exception e) {
            log.error("导出管理员数据失败", e);
            handleExportError(response, "导出管理员数据失败：" + e.getMessage());
        }
    }

    /**
     * 导出角色数据
     */
    @PostMapping("/export/role")
    @Operation(summary = "导出角色数据")
    public void exportRole(@RequestBody RoleQueryDto roleQueryDto, HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("角色数据", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 查询所有数据（不分页）
            // 注意：不能设置为null，因为PageHelper.startPage()不支持null值
            // 使用getRoleList()方法获取所有数据，避免分页
            List<RoleVo> roleList = roleService.getRoleList();

            // 转换为Excel数据格式
            List<RoleExcelData> excelDataList = roleList.stream().map(role -> {
                RoleExcelData excelData = new RoleExcelData();
                BeanUtils.copyProperties(role, excelData);
                return excelData;
            }).collect(Collectors.toList());

            // 写入Excel
            EasyExcel.write(response.getOutputStream(), RoleExcelData.class)
                    .sheet("角色数据")
                    .doWrite(excelDataList);

            log.info("导出角色数据成功，共{}条记录", excelDataList.size());
        } catch (Exception e) {
            log.error("导出角色数据失败", e);
            handleExportError(response, "导出角色数据失败：" + e.getMessage());
        }
    }

    /**
     * 导出权限数据
     */
    @PostMapping("/export/permission")
    @Operation(summary = "导出权限数据")
    public void exportPermission(@RequestBody PermissionQueryDto permissionQueryDto, HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("权限数据", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 查询所有数据（不分页）
            // 注意：不能设置为null，因为PageHelper.startPage()不支持null值
            // 使用getPermissionList()方法获取所有数据，避免分页
            List<PermissionVo> permissionList = permissionService.getPermissionList();

            // 转换为Excel数据格式
            List<PermissionExcelData> excelDataList = permissionList.stream().map(permission -> {
                PermissionExcelData excelData = new PermissionExcelData();
                BeanUtils.copyProperties(permission, excelData);
                return excelData;
            }).collect(Collectors.toList());

            // 写入Excel
            EasyExcel.write(response.getOutputStream(), PermissionExcelData.class)
                    .sheet("权限数据")
                    .doWrite(excelDataList);

            log.info("导出权限数据成功，共{}条记录", excelDataList.size());
        } catch (Exception e) {
            log.error("导出权限数据失败", e);
            handleExportError(response, "导出权限数据失败：" + e.getMessage());
        }
    }

    /**
     * 导入权限数据
     */
    @PostMapping("/import/permission")
    @Operation(summary = "导入权限数据")
    public Result importPermission(@RequestParam("file") MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), PermissionExcelData.class,
                new PermissionDataListener(permissionService)).sheet().doRead();
        return Result.buildSuccess("导入成功");
    }

    /**
     * 下载权限导入模板
     */
    @GetMapping("/template/permission")
    @Operation(summary = "下载权限导入模板")
    public void downloadPermissionTemplate(HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("权限导入模板", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 创建模板数据
            List<PermissionExcelData> templateData = List.of(
                    createPermissionTemplate("用户管理", "user:manage", "用户管理权限"),
                    createPermissionTemplate("角色管理", "role:manage", "角色管理权限"),
                    createPermissionTemplate("权限管理", "permission:manage", "权限管理权限")
            );

            // 写入Excel
            EasyExcel.write(response.getOutputStream(), PermissionExcelData.class)
                    .sheet("权限模板")
                    .doWrite(templateData);

            log.info("下载权限导入模板成功");
        } catch (Exception e) {
            log.error("下载权限导入模板失败", e);
            handleExportError(response, "下载权限导入模板失败：" + e.getMessage());
        }
    }

    /**
     * 创建权限模板数据
     */
    private PermissionExcelData createPermissionTemplate(String name, String code, String description) {
        PermissionExcelData template = new PermissionExcelData();
        template.setPermissionName(name);
        template.setPermissionCode(code);
        template.setDescription(description);
        return template;
    }

    /**
     * 处理导出错误
     */
    private void handleExportError(HttpServletResponse response, String errorMessage) {
        try {
            // 重置响应
            response.reset();
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            // 返回错误信息
            String errorJson = "{\"code\":500,\"msg\":\"" + errorMessage + "\",\"data\":null}";
            response.getWriter().write(errorJson);
            response.getWriter().flush();
        } catch (IOException e) {
            log.error("处理导出错误时发生异常", e);
        }
    }
}
