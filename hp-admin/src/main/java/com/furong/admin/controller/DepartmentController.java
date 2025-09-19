package com.furong.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.furong.admin.interceptor.AdminThreadLocal;
import com.furong.pojo.dto.DepartmentQueryDto;
import com.furong.pojo.entity.Department;
import com.furong.pojo.vo.AdminInfoVo;
import com.furong.result.Result;
import com.furong.service.DepartmentService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping("/list")
    public Result list(@RequestBody DepartmentQueryDto departmentQueryDto ) {
        Page <Department> page = departmentService.page(Page.of(departmentQueryDto.getPageNum(), departmentQueryDto.getPageSize()));

        QueryWrapper queryWrapper = QueryWrapper.create();

        queryWrapper.like(Department::getName,departmentQueryDto.getName(),
                !StrUtil.isEmpty(departmentQueryDto.getName()))
                .eq(Department::getType,departmentQueryDto.getType(),
                        departmentQueryDto.getType() != null)
                .eq(Department::getRecommended,departmentQueryDto.getRecommended(),
                        departmentQueryDto.getRecommended() != null);

        Page<Department> list = departmentService.page(page, queryWrapper);
        return Result.buildSuccess(list.getRecords(), list.getTotalRow());

    }

    @GetMapping("/find")
    public Result find(@RequestParam Long id){
        Department department = departmentService.getById(id);
        return Result.buildSuccess(department);
    }

    @GetMapping("/findAll")
    public Result findAll(){

        AdminInfoVo vo = AdminThreadLocal.get();

        return Result.buildSuccess(departmentService.list());
    }

    @PostMapping("/add")
    public Result add(@RequestBody Department department){
        boolean result = departmentService.save(department);
        return Result.jude(result);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Department department){
        boolean result = departmentService.updateById(department);
        return Result.jude(result);
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam Long id){
        boolean result = departmentService.removeById(id);
        return Result.jude(result);
    }
}
