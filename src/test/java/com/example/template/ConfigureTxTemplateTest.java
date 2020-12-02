package com.example.template;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class ConfigureTxTemplateTest {

    private static DriverManagerDataSource dataSource;
    private static PlatformTransactionManager transactionManager;

    @BeforeAll
    public static void configure() {
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test?characterEncoding=utf8");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        transactionManager = new DataSourceTransactionManager(dataSource);
    }

    @Test
    public void test() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);


        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            // [1] 插入纪录
            jdbcTemplate.execute("insert into EMPLOYEE(firstname,lastname,address) values('惬意1','公告1','丁真')");

            // [2] 范例抛出异常
            Integer i = 0;
            if (i.equals(0)) {
                throw new RuntimeException("插入失败");
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Test
    public void testWithoutTx() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        try {
            // [1] 插入纪录
            jdbcTemplate.execute("insert into EMPLOYEE(firstname,lastname,address) values('惬意2','公告','丁真')");
            // [2] 范例抛出异常
            Integer i = 0;
            if (i.equals(0)) {
                throw new RuntimeException("插入失败");
            }
        } catch (Exception e) {
            throw e;
        }
    }

}


