package com.furong.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 角色VO
 * @author Origami
 * @date 2025/9/13
 */
@Data
public class RoleVo {
    
    private Long id;
    
    private String roleName;
    
    private String description;
    
    private Date created;
    
    private Date updated;
    
    // 关联的权限列表
    private List<PermissionVo> permissions;
    
    // 拥有该角色的管理员数量
    private Integer adminCount;
}
