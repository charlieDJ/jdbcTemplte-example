package com.example.template.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标注于实体属性上，有助于自定义insert语句
 * @author dj
 * @date 2021/5/13
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface UnderlyingColumn {
    /**
     *
     * @return 列名
     */
    String name() default "";
}
