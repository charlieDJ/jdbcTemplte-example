package com.example.template.dao;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author dj
 * @date 2021/5/8
 */
@Documented
@Component
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JpaDto {
}
