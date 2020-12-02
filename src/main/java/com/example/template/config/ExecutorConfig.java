package com.example.template.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ExecutorConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*@Bean("myExecutor")
    public ThreadPoolExecutor executor(){
        Integer thread = jdbcTemplate.queryForObject("select score from t_user_score where id = 7", Integer.class);
        System.out.println("线程数量：" + thread);
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(thread, new NamedThreadFactory("myExecutor"));
    }*/

}
