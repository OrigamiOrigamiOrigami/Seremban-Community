package com.furong.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * 权限VO
 * @author Origami
 * @date 2025/9/13
 */
@Data
public class PermissionVo {
    
    private Long id;
    
    private String permissionName;
    
    private String permissionCode;
    
    private String description;
    
    private Date created;
    
    private Date updated;
    
    // 拥有该权限的角色数量
    private Integer roleCount;
}
