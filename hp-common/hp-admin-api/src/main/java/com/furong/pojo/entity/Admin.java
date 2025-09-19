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
 * @author Origami
 * @date 2025/9/9
 */
@Table("fr_admin")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Admin implements Serializable {

    @Id(keyType = KeyType.Auto)
    private Long id;
    
    @Column("name")
    private String name;
    
    @Column("username")
    private String username;
    
    @Column("phone")
    private String phone;
    
    @Column("email")
    private String email;
    
    @Column("password")
    private String password;
    
    @Column("salt")
    private String salt;
    
    @Column(value = "created", onInsertValue = "now()")
    private Date created;
    
    @Column(value = "updated", onUpdateValue = "now()", onInsertValue = "now()")
    private Date updated;
    
    @Column("tenant_id")
    private Long tenantId;
    
    @Column("image")
    private String image;
    
    @Column(value = "deleted", isLogicDelete = true)
    private Integer deleted;
}
