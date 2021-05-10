package com.example.template.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author dj
 * @date 2021/5/10
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractUser {
    @CreatedBy
    private String createBy;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @LastModifiedBy
    private String updateBy;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
}
