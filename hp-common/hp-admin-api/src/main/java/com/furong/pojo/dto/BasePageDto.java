package com.furong.pojo.dto;

import lombok.Data;

@Data
public class BasePageDto {

    private Integer pageNum;

    private Integer pageSize;

    public Integer getPage() {
        return pageNum;
    }

    public Integer getSize() {
        return pageSize;
    }
}
