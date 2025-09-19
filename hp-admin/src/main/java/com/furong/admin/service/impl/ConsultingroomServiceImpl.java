package com.furong.admin.service.impl;

import com.furong.admin.mapper.ConsultingroomMapper;
import com.furong.pojo.dto.ConsultingroomQueryDto;
import com.furong.pojo.entity.Consultingroom;
import com.furong.pojo.vo.ConsultingroomVo;
import com.furong.service.ConsultingroomService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultingroomServiceImpl extends ServiceImpl<ConsultingroomMapper, Consultingroom> implements ConsultingroomService {

    @Autowired
    private ConsultingroomMapper consultingroomMapper;

    @Override
    public Page<ConsultingroomVo> selectConsultingroomPage(ConsultingroomQueryDto queryDto) {
        // 使用PageHelper分页助手
        PageHelper.startPage(queryDto.getPageNum(), queryDto.getPageSize());
        return consultingroomMapper.selectConsultingroomPage(queryDto);
    }
}
