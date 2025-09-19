package com.furong.pojo.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Origami
 * @date 2025/9/10
 */
@Data
public class AdminLoginDto implements Serializable {

    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "用户名格式不正确!")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "密码格式不正确!")
    private String password;

}
