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

@Data
@Table("fr_consultingroom")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Consultingroom implements Serializable {

    @Id(keyType = KeyType.Auto)
    private Long id;

    @Column("name")
    private String name;

    @Column("department_id")
    private Long departmentId;

    @Column("description")
    private String description;

    @Column("status")
    private Integer status;

    @Column(value = "created", onInsertValue = "now()")
    private Date created;

    @Column(value = "updated", onUpdateValue = "now()", onInsertValue = "now()")
    private Date updated;

    @Column(value = "deleted", isLogicDelete = true)
    private Integer deleted;

    @Column("tenant_id")
    private Long tenantId;
}
