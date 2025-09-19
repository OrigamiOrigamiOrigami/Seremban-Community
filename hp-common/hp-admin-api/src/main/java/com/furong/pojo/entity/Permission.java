package com.furong.pojo.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限实体类
 * @author Origami
 * @date 2025/9/13
 */
@Table("fr_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permission implements Serializable {

    @Id(keyType = KeyType.Auto)
    private Long id;
    
    @Column("permission_name")
    private String permissionName;
    
    @Column("permission_code")
    private String permissionCode;
    
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
