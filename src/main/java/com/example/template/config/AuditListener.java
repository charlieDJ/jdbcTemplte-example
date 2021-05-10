package com.example.template.config;

import com.example.template.entity.Audit;
import com.example.template.entity.Auditable;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * @author dj
 * @date 2021/5/10
 */
public class AuditListener {

    @PrePersist
    public void setCreatedOn(Object target) {
        Auditable auditable = (Auditable) target;
        Audit audit = auditable.getAudit();
        if (audit == null) {
            audit = new Audit();
            auditable.setAudit(audit);
        }
        // 从session中获取用户
        audit.setCreateBy("zhangsan");
        audit.setCreateName("张三");
        audit.setCreateFullName("张三丰");
        audit.setCreateTime(LocalDateTime.now());
        audit.setUpdateBy("zhangsan");
        audit.setUpdateName("张三");
        audit.setUpdateFullName("张三丰");
        audit.setUpdateTime(LocalDateTime.now());
    }


    @PreUpdate
    public void setUpdatedOn(Object target) {
        Auditable auditable = (Auditable) target;
        Audit audit = auditable.getAudit();
        // 从session中获取用户
        audit.setUpdateBy("zhangsan");
        audit.setUpdateName("张三");
        audit.setUpdateFullName("张三丰");
        audit.setUpdateTime(LocalDateTime.now());
    }
}
