package com.example.template.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author dj
 * @date 2021/1/26
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
public class MyVo {
    private boolean justice;
    private String name;

    public MyVo(String name) {
        this.name = name;
    }
}
