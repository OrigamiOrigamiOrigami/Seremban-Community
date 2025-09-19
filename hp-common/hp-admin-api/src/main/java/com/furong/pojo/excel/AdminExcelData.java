package com.furong.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 管理员导出Excel数据
 * @author Origami
 * @date 2025/9/13
 */
@Data
public class AdminExcelData {

    @ExcelProperty("管理员ID")
    private Long id;

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("角色")
    private String roleNames;

    @ExcelProperty("创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date created;

    @ExcelProperty("更新时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updated;
}
