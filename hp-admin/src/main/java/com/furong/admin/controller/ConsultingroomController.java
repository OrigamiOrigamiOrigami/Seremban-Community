package com.furong.admin.controller;

import com.furong.admin.interceptor.AdminThreadLocal;
import com.furong.annotation.RequirePermission;
import com.furong.pojo.dto.ConsultingroomQueryDto;
import com.furong.pojo.entity.Consultingroom;
import com.furong.pojo.vo.ConsultingroomVo;
import com.furong.result.Result;
import com.furong.service.ConsultingroomService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultingroom")
public class ConsultingroomController {

    @Autowired
    private ConsultingroomService consultingroomService;

    @RequestMapping("/list")
    @RequirePermission("consultingroom:manage")
    public Result list(@RequestBody ConsultingroomQueryDto consultingroomQueryDto) {
        // 设置租户id（从当前登录用户获取）
        // 这是手动设置租户ID的方式，MyBatis-Flex会自动处理大部分情况
        consultingroomQueryDto.setTenantId(AdminThreadLocal.get().getTenantId());

        Page<ConsultingroomVo> page = consultingroomService.selectConsultingroomPage(consultingroomQueryDto);
        return Result.buildSuccess(page.getResult(), page.getTotal());
    }

    @GetMapping("/find")
    public Result find(@RequestParam Long id) {
        Consultingroom consultingroom = consultingroomService.getById(id);
        return Result.buildSuccess(consultingroom);
    }

    @GetMapping("/findAll")
    public Result findAll() {
        return Result.buildSuccess(consultingroomService.list());
    }

    @PostMapping("/add")
    public Result add(@RequestBody Consultingroom consultingroom) {
        boolean result = consultingroomService.save(consultingroom);
        return Result.jude(result);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Consultingroom consultingroom) {
        boolean result = consultingroomService.updateById(consultingroom);
        return Result.jude(result);
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam Long id) {
        boolean result = consultingroomService.removeById(id);
        return Result.jude(result);
    }
}
