package com.furong.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 角色导出Excel数据
 * @author Origami
 * @date 2025/9/13
 */
@Data
public class RoleExcelData {

    @ExcelProperty("角色ID")
    private Long id;

    @ExcelProperty("角色名称")
    private String roleName;

    @ExcelProperty("角色描述")
    private String description;

    @ExcelProperty("管理员数量")
    private Integer adminCount;

    @ExcelProperty("创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date created;

    @ExcelProperty("更新时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updated;
}
