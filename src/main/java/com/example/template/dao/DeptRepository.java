package com.example.template.dao;

import com.example.template.entity.Dept;
import com.example.template.model.DeptVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dj
 * @date 2021/5/8
 */
@Repository
public interface DeptRepository extends JpaRepository<Dept, Integer> {

    @Query(value = "select id,dept_name deptName from dept where dept_name = ?1", nativeQuery = true)
    List<DeptVo> findDeptByName(String name);

    List<Dept> findByDeptName(String deptName);
}
