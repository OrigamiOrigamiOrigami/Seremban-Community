package com.furong.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * 批量删除DTO
 */
@Data
public class BaseDeleteDto {
    
    private List<Long> ids;
}
