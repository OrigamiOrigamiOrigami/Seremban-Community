package com.furong.service;

import com.furong.pojo.dto.ConsultingroomQueryDto;
import com.furong.pojo.entity.Consultingroom;
import com.furong.pojo.vo.ConsultingroomVo;
import com.github.pagehelper.Page;
import com.mybatisflex.core.service.IService;

public interface ConsultingroomService extends IService<Consultingroom> {

    /**
     * 分页查询诊室列表（关联科室名称）
     */
    Page<ConsultingroomVo> selectConsultingroomPage(ConsultingroomQueryDto queryDto);
}
