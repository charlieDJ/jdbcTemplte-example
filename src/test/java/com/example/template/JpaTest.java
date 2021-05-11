package com.example.template;

import com.example.template.dao.DeptRepository;
import com.example.template.dao.EmployeeRepository;
import com.example.template.entity.Employee;
import com.example.template.model.DeptVo;
import com.example.template.model.EmployeeVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author dj
 * @date 2021/5/8
 */
@Slf4j
public class JpaTest extends TemplateApplicationTests {


    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DeptRepository deptRepository;

    @Test
    public void save(){
        Employee employee = new Employee();
        employee.setFirstName("auditTest")
                .setLastName("auditTest")
                .setDeptId(1)
                .setAddress("天池");
        employeeRepository.save(employee);
        System.out.println(employee.getId());
    }

    @Test
    public void saveAll(){
        List<Employee> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Employee employee = new Employee();
            employee.setFirstName("lei" + i)
                    .setLastName("tong")
                    .setDeptId(1)
                    .setAddress("天池");
            list.add(employee);
        }
        employeeRepository.saveAll(list);
    }

    @Test
    public void update(){
        Employee employee = employeeRepository.findById(10).get();
        employee.setAddress("观音桥");
        employeeRepository.save(employee);
    }

    @Test
    public void delete(){
        // 传入对象删除
        Employee employee = employeeRepository.findById(13).get();
        employeeRepository.delete(employee);
        // 传入主键删除
        employeeRepository.deleteById(12);
    }

    @Test
    public void query() {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    @Test
    public void findById() {
        Optional<Employee> employeeOpt = employeeRepository.findById(1);
        Employee employee = employeeOpt.get();
        System.out.println(employee);
    }

    @Test
    public void findByAddress() {
        List<Employee> employees = employeeRepository.findByAddress("观音桥");
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    @Test
    public void findByLastname() {
        List<Employee> employees = employeeRepository.findByLastName("gui");
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    @Test
    public void findByDeptId() {
        List<EmployeeVo> employees = employeeRepository.findByDeptId(1);
        for (EmployeeVo employee : employees) {
            System.out.println(employee);
        }
    }

    @Test
    public void findByIdNotExist() {
        Assertions.assertThrows(RuntimeException.class, () ->
                employeeRepository.findById(1001)
                        .orElseThrow(() -> new RuntimeException("用户不存在"))
        );
    }

    @Test
    public void getEmployeeByDeptId() {
        List<Map<String, Object>> maps = employeeRepository.getByDeptId(1);
        for (Map<String, Object> employeeVo : maps) {
            System.out.println(employeeVo);
        }
    }

    @Test
    public void getEmployeeVoByDeptId() {
        List<EmployeeVo> employeeVos = employeeRepository.getVoByDeptId(1);
        for (EmployeeVo employeeVo : employeeVos) {
            log.info(employeeVo.toString());
        }
    }

    @Test
    public void getByDynamic(){
        List<Employee> employees = employeeRepository.findDynamically("", "lei1", "天池");
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    @Test
    public void getDeptByName(){
        List<DeptVo> deptVos = deptRepository.findDeptByName("研发");
        for (DeptVo deptVo : deptVos) {
            System.out.println(deptVo.getDeptName());
        }
    }

    @Test
    public void saveBatch(){
        List<Employee> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Employee employee = new Employee();
            employee.setFirstName("lei" + i)
                    .setLastName("tong")
                    .setDeptId(1)
                    .setAddress("天池");
            list.add(employee);
        }
        employeeRepository.saveBatch(list);
    }

}
