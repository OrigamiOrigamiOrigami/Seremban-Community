package com.furong.pojo.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 管理员角色关联实体类
 * @author Origami
 * @date 2025/9/13
 */
@Table("fr_admin_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminRole implements Serializable {

    @Column("admin_id")
    private Long adminId;
    
    @Column("role_id")
    private Long roleId;
    
    @Column("tenant_id")
    private Long tenantId;
}
