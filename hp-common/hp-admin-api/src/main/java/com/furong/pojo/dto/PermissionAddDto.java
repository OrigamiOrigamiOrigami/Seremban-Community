package com.furong.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限添加DTO
 * @author Origami
 * @date 2025/9/13
 */
@Data
public class PermissionAddDto implements Serializable {

    @NotBlank(message = "权限名称不能为空!")
    private String permissionName;

    @NotBlank(message = "权限标识不能为空!")
    @Pattern(regexp = "^[a-zA-Z0-9:_-]+$", message = "权限标识格式不正确，只能包含字母、数字、冒号、下划线和横线!")
    private String permissionCode;

    private String description;
}
