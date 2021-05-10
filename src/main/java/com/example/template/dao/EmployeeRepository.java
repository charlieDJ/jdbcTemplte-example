package com.example.template.dao;

import com.example.template.entity.Employee;
import com.example.template.model.EmployeeVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author dj
 * @date 2021/5/8
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, EmployeeRepositoryCustom {
    @Query(value = "select e.*,d.dept_name  from EMPLOYEE e left join " +
            "dept d on e.dept_id = d.id where d.id = ?1", nativeQuery = true)
    List<EmployeeVo> getVoByDeptId(Integer deptId);

    @Query(value = "select e.*,d.dept_name from EMPLOYEE e left join " +
            "dept d on e.dept_id = d.id where d.id = ?1", nativeQuery = true)
    List<Map<String, Object>> getByDeptId(Integer deptId);

    List<Employee> findByAddress(@Param("address") String address);

    @Query(value = "select * from EMPLOYEE where lastname = :lastname", nativeQuery = true)
    List<Employee> findByLastName(@Param("lastname") String lastname);
}
