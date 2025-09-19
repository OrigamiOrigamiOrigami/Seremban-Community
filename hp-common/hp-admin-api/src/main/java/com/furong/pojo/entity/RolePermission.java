package com.furong.pojo.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色权限关联实体类
 * @author Origami
 * @date 2025/9/13
 */
@Table("fr_role_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolePermission implements Serializable {

    @Column("role_id")
    private Long roleId;
    
    @Column("permission_id")
    private Long permissionId;
    
    @Column("tenant_id")
    private Long tenantId;
}
