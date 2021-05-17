package com.example.template.entity;

import com.example.template.annotation.UnderlyingColumn;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author dj
 * @date 2021/5/13
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
@Table(name = "test")
public class TestJoinColumn {
    private int id;
    @Transient
    @UnderlyingColumn(name = "person_id")
    private String personId;
}
