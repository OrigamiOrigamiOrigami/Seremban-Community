package com.furong.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ConsultingroomVo {
    
    private Long id;
    
    private String name;
    
    private Long departmentId;
    
    private String departmentName;  // 关联查询的科室名称
    
    private String description;
    
    private Integer status;
    
    private Date created;
    
    private Date updated;
}
