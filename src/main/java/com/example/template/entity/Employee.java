package com.example.template.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author dj
 * @date 2021/1/26
 */
@Entity
@Data
@ToString
@Accessors(chain = true)
@Table(name = "EMPLOYEE")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    @Column(name = "dept_id")
    private Integer deptId;
}
