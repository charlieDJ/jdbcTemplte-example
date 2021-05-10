package com.example.template.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

/**
 * @author dj
 * @date 2021/5/10
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
@Embeddable
public class Audit {
    private String createBy;
    private String createName;
    private String createFullName;
    private LocalDateTime createTime;
    private String updateBy;
    private String updateName;
    private String updateFullName;
    private LocalDateTime updateTime;
}
