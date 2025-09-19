package com.furong.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分配角色DTO
 * @author Origami
 * @date 2025/9/13
 */
@Data
public class AssignRoleDto implements Serializable {

    @NotNull(message = "管理员ID不能为空!")
    private Long adminId;
    
    // 角色ID列表
    private List<Long> roleIds;
}
