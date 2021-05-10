package com.example.template.dao;

import com.example.template.entity.Employee;
import com.example.template.model.EmployeeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dj
 * @date 2021/5/8
 */
@Component
public class EmployeeRepositoryCustomImpl implements EmployeeRepositoryCustom {

    @Autowired
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    @Override
    public List<Employee> findDynamically(String lastName, String firstName, String address) {
        String sql = "select * from EMPLOYEE";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("lastName", lastName)
                .addValue("firstName", firstName)
                .addValue("address", address);
        List<String> criteriaList = new ArrayList<>();
        if (StringUtils.hasText(lastName)) {
            criteriaList.add(" lastname = :lastName");
        }
        if (StringUtils.hasText(firstName)) {
            criteriaList.add(" firstname = :firstName");
        }
        if (StringUtils.hasText(address)) {
            criteriaList.add(" address = :address");
        }
        String whereClause = criteriaList.stream()
                .collect(Collectors.joining(" AND ", " where ", ""));
        sql = sql + whereClause;
        System.out.println("sql: " + sql);
        return parameterJdbcTemplate.query(sql, source, BeanPropertyRowMapper.newInstance(Employee.class));
    }

    @Override
    public List<EmployeeVo> findByDeptId(int deptId) {
        String sql = "select e.*,d.dept_name from EMPLOYEE e left join dept d on e.dept_id = d.id where e.dept_id = :deptId";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("deptId", deptId);
        return parameterJdbcTemplate.query(sql, source, BeanPropertyRowMapper.newInstance(EmployeeVo.class));
    }
}
