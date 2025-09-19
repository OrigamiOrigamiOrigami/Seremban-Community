package com.furong.pojo.dto;

import lombok.Data;

@Data
public class ConsultingroomQueryDto extends BasePageDto {

    //诊室名称
    private String name;

    //科室id
    private Long departmentId;

    private Integer status;

    //租户id（用于多租户隔离）
    private Long tenantId;
}
