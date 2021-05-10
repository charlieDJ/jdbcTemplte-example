package com.example.template.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author dj
 * @date 2021/5/10
 */
@Configuration
@Component
public class UserAuditor implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // 这里可以从session中获取用户
        return Optional.of("zhangsan");
    }
}
