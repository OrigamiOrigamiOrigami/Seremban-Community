package com.furong.pojo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Origami
 * @date 2025/9/10
 */
@Data
public class AdminAddDto implements Serializable {

    @NotBlank(message = "管理员名称不能为空!")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "用户名格式不正确!")
    private String username;

    @Pattern(regexp = "^1(3[0-9]|4[579]|5[0-3,5-9]|6[2567]|7[0-3,5-8]|8[0-9]|9[0-3,5-9])\\d{8}$", message = "手机号格式不正确!")
    private String phone;

    @Email(message = "邮箱格式不合法!")
    private String email;

    @NotBlank(message = "头像不能为空!")
    private String image;

}
