package com.example.template.mapper;

import com.example.template.model.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();

        employee.setId(rs.getLong("ID"));
        employee.setFirstName(rs.getString("FIRSTNAME"));
        employee.setLastName(rs.getString("LASTNAME"));
        employee.setAddress(rs.getString("ADDRESS"));

        return employee;
    }
}
