package com.furong.admin.listener;

import com.furong.admin.utils.DigesterUtils;
import com.furong.pojo.entity.Admin;
import com.furong.pojo.entity.Role;
import com.furong.pojo.entity.Permission;
import com.furong.service.AdminService;
import com.furong.service.RoleService;
import com.furong.service.PermissionService;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员初始化监听器
 * 在应用启动时自动创建默认的admin账号
 * 
 * @author Origami
 * @date 2025/9/11
 */
@Component
@Slf4j
public class AdminInitListener implements ApplicationRunner {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始检查并初始化默认管理员账号和权限系统...");

        try {
            // 1. 初始化基础权限
            initializePermissions();

            // 2. 初始化基础角色
            initializeRoles();

            // 3. 检查是否已存在admin用户
            QueryWrapper queryWrapper = QueryWrapper.create();
            queryWrapper.eq(Admin::getUsername, "admin");
            Admin existingAdmin = adminService.getOne(queryWrapper);

            if (existingAdmin != null) {
                log.info("默认管理员账号已存在，跳过初始化。用户名: {}, 姓名: {}",
                    existingAdmin.getUsername(), existingAdmin.getName());
                return;
            }

            // 4. 创建默认admin账号并分配超级管理员角色
            createDefaultAdminWithRole();

        } catch (Exception e) {
            log.error("初始化默认管理员账号和权限系统失败", e);
        }
    }
    
    /**
     * 初始化基础权限
     */
    private void initializePermissions() {
        log.info("开始初始化基础权限...");

        // 定义基础权限
        String[][] permissions = {
            {"管理员管理", "admin:manage", "管理员的增删改查权限"},
            {"角色管理", "role:manage", "角色的增删改查权限"},
            {"权限管理", "permission:manage", "权限的增删改查权限"},
            {"科室管理", "department:manage", "科室的增删改查权限"},
            {"诊室管理", "consultingroom:manage", "诊室的增删改查权限"},
            {"系统查看", "system:view", "系统基本信息查看权限"},
            {"个人中心", "profile:manage", "个人信息管理权限"}
        };

        for (String[] permData : permissions) {
            // 检查权限是否已存在
            Permission existingPerm = permissionService.getByPermissionCode(permData[1]);
            if (existingPerm == null) {
                Permission permission = Permission.builder()
                        .permissionName(permData[0])
                        .permissionCode(permData[1])
                        .description(permData[2])
                        .tenantId(1L)
                        .build();
                permissionService.save(permission);
                log.info("创建权限: {} [{}]", permData[0], permData[1]);
            }
        }

        log.info("基础权限初始化完成");
    }

    /**
     * 初始化基础角色
     */
    private void initializeRoles() {
        log.info("开始初始化基础角色...");

        // 定义基础角色
        String[][] roles = {
            {"超级管理员", "系统超级管理员，拥有所有权限"},
            {"系统管理员", "系统管理员，负责系统管理相关功能"},
            {"科室管理员", "科室管理员，负责科室和诊室管理"},
            {"普通用户", "普通用户，只有基础查看权限"}
        };

        for (String[] roleData : roles) {
            // 检查角色是否已存在
            QueryWrapper roleQuery = QueryWrapper.create();
            roleQuery.eq(Role::getRoleName, roleData[0]);
            Role existingRole = roleService.getOne(roleQuery);

            if (existingRole == null) {
                Role role = Role.builder()
                        .roleName(roleData[0])
                        .description(roleData[1])
                        .tenantId(1L)
                        .build();
                roleService.save(role);
                log.info("创建角色: {}", roleData[0]);

                // 为超级管理员角色分配所有权限
                if ("超级管理员".equals(roleData[0])) {
                    assignAllPermissionsToSuperAdmin(role.getId());
                }
            }
        }

        log.info("基础角色初始化完成");
    }

    /**
     * 为超级管理员角色分配所有权限
     */
    private void assignAllPermissionsToSuperAdmin(Long superAdminRoleId) {
        log.info("为超级管理员角色分配所有权限...");

        // 获取所有权限ID
        List<Permission> allPermissions = permissionService.list();
        List<Long> permissionIds = allPermissions.stream()
                .map(Permission::getId)
                .collect(Collectors.toList());

        if (!permissionIds.isEmpty()) {
            roleService.assignPermissions(superAdminRoleId, permissionIds);
            log.info("为超级管理员角色分配了 {} 个权限", permissionIds.size());
        }
    }

    /**
     * 创建默认的admin账号并分配超级管理员角色
     */
    private void createDefaultAdminWithRole() {
        log.info("开始创建默认管理员账号...");

        // 生成盐
        String salt = DigesterUtils.createSalt();

        // 默认密码：123456
        String defaultPassword = "123456";
        String encryptedPassword = DigesterUtils.md5(defaultPassword, salt);

        // 创建admin用户
        Admin admin = Admin.builder()
                .name("系统管理员")
                .username("admin")
                .phone("13800138000")
                .email("admin@hospital.com")
                .password(encryptedPassword)
                .salt(salt)
                .image("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png") // 默认头像
                .tenantId(1L)
                .build();

        // 保存到数据库
        boolean result = adminService.save(admin);

        if (result) {
            log.info("默认管理员账号创建成功！");

            // 为admin分配超级管理员角色
            assignSuperAdminRole(admin.getId());

            log.info("用户名: admin");
            log.info("密码: 123456");
            log.info("姓名: 系统管理员");
            log.info("手机: 13800138000");
            log.info("邮箱: admin@hospital.com");
            log.info("角色: 超级管理员");
            log.info("请在首次登录后及时修改密码！");
        } else {
            log.error("默认管理员账号创建失败！");
        }
    }

    /**
     * 为admin分配超级管理员角色
     */
    private void assignSuperAdminRole(Long adminId) {
        log.info("为默认管理员分配超级管理员角色...");

        // 查找超级管理员角色
        QueryWrapper roleQuery = QueryWrapper.create();
        roleQuery.eq(Role::getRoleName, "超级管理员");
        Role superAdminRole = roleService.getOne(roleQuery);

        if (superAdminRole != null) {
            // 创建分配角色DTO
            com.furong.pojo.dto.AssignRoleDto assignRoleDto = new com.furong.pojo.dto.AssignRoleDto();
            assignRoleDto.setAdminId(adminId);
            assignRoleDto.setRoleIds(Arrays.asList(superAdminRole.getId()));

            // 分配角色
            roleService.assignRoles(assignRoleDto);
            log.info("成功为默认管理员分配超级管理员角色");
        } else {
            log.error("未找到超级管理员角色！");
        }
    }
}
