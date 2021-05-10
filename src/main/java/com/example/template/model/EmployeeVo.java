package com.example.template.model;

import com.example.template.dao.JpaDto;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author dj
 * @date 2021/1/26
 */
@Data
@ToString
@Accessors(chain = true)
@JpaDto
public class EmployeeVo {
    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    private String deptName;
}
