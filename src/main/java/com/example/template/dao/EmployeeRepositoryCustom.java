package com.example.template.dao;

import com.example.template.entity.Employee;
import com.example.template.model.EmployeeVo;

import java.util.List;

/**
 * @author dj
 * @date 2021/5/8
 */
public interface EmployeeRepositoryCustom {
    List<Employee> findDynamically(String lastName, String firstName, String address);

    List<EmployeeVo> findByDeptId(int deptId);
}
