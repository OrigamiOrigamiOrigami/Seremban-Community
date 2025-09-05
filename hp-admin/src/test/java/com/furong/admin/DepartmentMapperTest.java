package com.furong.admin;

import com.furong.admin.mapper.DepartmentMapper;
import com.furong.pojo.entity.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class DepartmentMapperTest {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Test
    public void insertMapper() {
        Department department = Department.builder()
                .name("测试")
                .type(1)
                .description("测试")
                .recommended(0)
                .tenantId(1L)
                .build();
        int result = departmentMapper.insertSelective(department);
        System.out.println(result);
    }

    @Test
    public void deleteMapper() {
        departmentMapper.deleteById(1L);
    }

    @Test
    public void updateMapper() {
        Department department = new Department();
        department.setId(21L);
        department.setName("测试555");
        departmentMapper.update(department);

    }

    @Test
    public void selectMapper() {
        List<Department> department = departmentMapper.selectAll();
        System.out.println(department);
    }
}
