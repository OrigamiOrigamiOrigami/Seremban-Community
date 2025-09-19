package com.furong.pojo.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.annotation.TenantId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色实体类
 * @author Origami
 * @date 2025/9/13
 */
@Table("fr_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role implements Serializable {

    @Id(keyType = KeyType.Auto)
    private Long id;
    
    @Column("role_name")
    private String roleName;
    
    @Column("description")
    private String description;
    
    @Column(value = "created", onInsertValue = "now()")
    private Date created;
    
    @Column(value = "updated", onUpdateValue = "now()", onInsertValue = "now()")
    private Date updated;
    
    @Column(value = "deleted", isLogicDelete = true)
    private Integer deleted;
    
    @Column("tenant_id")
    private Long tenantId;
}
