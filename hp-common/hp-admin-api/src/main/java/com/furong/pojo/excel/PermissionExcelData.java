package com.furong.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 权限导出Excel数据
 * @author Origami
 * @date 2025/9/13
 */
@Data
public class PermissionExcelData {

    @ExcelProperty("权限ID")
    private Long id;

    @ExcelProperty("权限名称")
    private String permissionName;

    @ExcelProperty("权限标识")
    private String permissionCode;

    @ExcelProperty("权限描述")
    private String description;

    @ExcelProperty("角色数量")
    private Integer roleCount;

    @ExcelProperty("创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date created;

    @ExcelProperty("更新时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updated;
}
