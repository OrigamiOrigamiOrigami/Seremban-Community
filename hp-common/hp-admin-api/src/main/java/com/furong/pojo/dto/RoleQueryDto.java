package com.furong.pojo.dto;

import lombok.Data;

/**
 * 角色查询DTO
 * @author Origami
 * @date 2025/9/13
 */
@Data
public class RoleQueryDto extends BasePageDto {
    
    // 角色名称
    private String roleName;
    
    // 角色描述
    private String description;
}
