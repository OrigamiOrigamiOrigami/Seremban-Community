package com.furong.pojo.dto;

import lombok.Data;

@Data
public class DepartmentQueryDto extends BasePageDto{
    private String name;

    private Integer type;

    private Integer recommended;
}
