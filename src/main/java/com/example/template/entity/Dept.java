package com.example.template.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author dj
 * @date 2021/5/8
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "dept")
public class Dept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tel;
    private String address;
    @Column(name = "dept_name")
    private String deptName;

}
