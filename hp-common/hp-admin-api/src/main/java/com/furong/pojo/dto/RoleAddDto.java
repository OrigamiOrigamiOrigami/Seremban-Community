package com.furong.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色添加DTO
 * @author Origami
 * @date 2025/9/13
 */
@Data
public class RoleAddDto implements Serializable {

    @NotBlank(message = "角色名称不能为空!")
    private String roleName;

    private String description;
    
    // 权限ID列表
    private List<Long> permissionIds;
}
