package com.furong.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 管理员信息VO（包含角色信息）
 * @Author Origami
 * @Date 2025/09/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminInfoVo implements Serializable {

    private long id;

    private String username;

    private String phone;

    private String name;

    private String email;

    private String image;

    private Date created;

    private Date updated;

    // 租户ID（用于多租户隔离）
    private Long tenantId;

    // 关联的角色列表
    private List<RoleVo> roles;

    // 角色名称字符串（用于显示）
    private String roleNames;
}
