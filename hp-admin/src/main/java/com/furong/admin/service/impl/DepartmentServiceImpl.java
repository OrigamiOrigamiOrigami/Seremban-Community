package com.furong.admin.service.impl;

import com.furong.admin.mapper.DepartmentMapper;
import com.furong.service.DepartmentService;
import com.furong.pojo.entity.Department;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
}
