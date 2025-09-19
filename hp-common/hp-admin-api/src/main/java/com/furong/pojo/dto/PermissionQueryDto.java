package com.furong.pojo.dto;

import lombok.Data;

/**
 * 权限查询DTO
 * @author Origami
 * @date 2025/9/13
 */
@Data
public class PermissionQueryDto extends BasePageDto {
    
    // 权限名称
    private String permissionName;
    
    // 权限标识
    private String permissionCode;
    
    // 权限描述
    private String description;
}
