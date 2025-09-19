package com.furong.pojo.dto;

import lombok.Data;

/**
 * @author Origami
 * @date 2025/9/9
 */
@Data
public class AdminQueryDto extends BasePageDto {
    
    // 姓名
    private String name;
    
    // 手机号码
    private String phone;
}
