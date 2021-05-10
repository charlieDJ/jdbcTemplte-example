package com.example.template;

import com.example.template.entity.Employee;
import com.example.template.function.SupplierWrapper;
import com.example.template.mapper.EmployeeRowMapper;
import com.example.template.page.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class TemplateTest extends TemplateApplicationTests {

    @Autowired
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void insert() {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Employee employee = new Employee();
        employee.setFirstName("bill" + 1).setLastName("gates" + 1).setAddress("china" + 1);
        SqlParameterSource source = new BeanPropertySqlParameterSource(employee);
        String sql = "insert into EMPLOYEE(firstname,lastname,address) values(:firstName,:lastName,:address)";
        parameterJdbcTemplate.update(sql, source, keyHolder, new String[]{"id"});
        System.out.println(keyHolder.getKey());
    }

    @Test
    public void count() {
        Integer count = jdbcTemplate.queryForObject("select count(1) from EMPLOYEE", Integer.class);
        System.out.println("count: " + count);
    }

    @Test
    public void selectForRows() {
        EmptySqlParameterSource source = new EmptySqlParameterSource();
        Employee employee = parameterJdbcTemplate.queryForObject("select * from EMPLOYEE where id = 1", source, new EmployeeRowMapper());
        System.out.println(employee);
    }


    @Test
    public void selectForProperties() {
        EmptySqlParameterSource source = new EmptySqlParameterSource();
        BeanPropertyRowMapper<Employee> rowMapper = BeanPropertyRowMapper.newInstance(Employee.class);
        List<Employee> employee = parameterJdbcTemplate.query("select id,lastname,firstname,address from EMPLOYEE", source, rowMapper);
        for (Employee employee1 : employee) {
            System.out.println(employee1);
        }
    }

    @Test
    public void selectByPage() {
        EmptySqlParameterSource source = new EmptySqlParameterSource();
        BeanPropertyRowMapper<Employee> rowMapper = BeanPropertyRowMapper.newInstance(Employee.class);
        String columns = "id,lastname,firstname,address";
        String afterFromSql = "EMPLOYEE";
        SqlBuilder build = new SqlBuilder.Builder().setColumns(columns)
                .setAfterFromSql(afterFromSql).build();
        Integer count = jdbcTemplate.queryForObject(build.getCountSql(), Integer.class);
        System.out.println("count: " + count);
        Page page = PageFactory.newInstance(MySqlPage.class);
        String pagedSql = page.startPage(build.getSql(), 2, 3);
        System.out.println("pagedSql: " + pagedSql);
        List<Employee> employee = parameterJdbcTemplate.query(pagedSql, source, rowMapper);
        System.out.println(employee);
    }

    @Test
    public void query() {
        MapSqlParameterSource mapParameter = new MapSqlParameterSource();
        mapParameter.addValue("id", 1);
        BeanPropertyRowMapper<Employee> rowMapper = BeanPropertyRowMapper.newInstance(Employee.class);
        String columns = "id,lastname,firstname,address";
        String afterFromSql = "EMPLOYEE where id = :id";
        SqlBuilder build = new SqlBuilder.Builder().setColumns(columns)
                .setAfterFromSql(afterFromSql).build();
        Employee wrappedEmployee = SupplierWrapper.wrap(() -> parameterJdbcTemplate.queryForObject(build.getSql(), mapParameter, rowMapper));
        System.out.println(wrappedEmployee);
    }

    @Test
    public void updateBatch(){
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Employee employee = new Employee();
            employee.setFirstName("bill" + i)
                    .setLastName("gates" + i)
                    .setAddress("china" + i);
            employees.add(employee);
        }
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(employees);
        String sql = "INSERT INTO EMPLOYEE(firstname,lastname,address) VALUES (:firstName, :lastName, :address)";
        int[] updateCounts = parameterJdbcTemplate.batchUpdate(sql, batch);
        System.out.println("updateCounts: " + Arrays.toString(updateCounts));
    }

    @Test
    public void like(){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource() {{
            addValue("firstname", "bill1%");
        }};
        SqlBuilder sqlBuilder = new SqlBuilder.Builder().setColumns("*")
                .setAfterFromSql("EMPLOYEE where firstname like :firstname").build();
        List<Employee> employees = parameterJdbcTemplate.query(sqlBuilder.getSql(), parameterSource, BeanPropertyRowMapper.newInstance(Employee.class));
        System.out.println(employees);
    }

    @Test
    public void oraclePage(){
        String sql = "select 1 from dual";
        Page page = PageFactory.newInstance(OraclePage.class);
        String pageSql = page.startPage(sql, 1, 20);
        System.out.println("分页：" + pageSql);
    }

    @Test
    public void newQuery(){
        SqlBuilder.Builder builder = SqlBuilder.newInstance();
        List<String> columns = Stream.of("firstname",
                "address").collect(Collectors.toList());
        builder.setColumns(columns)
                .setAfterFromSql("EMPLOYEE")
                .where("firstname = 'berry'")
                .where("address = 'nanshan'");
        SqlBuilder build = builder.build();
        log.info("sql:{},countSql:{}", build.getSql(), build.getCountSql());
    }




}
