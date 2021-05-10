package com.example.template.entity;

/**
 * @author dj
 * @date 2021/5/10
 */
public interface Auditable {
    Audit getAudit();

    void setAudit(Audit audit);
}
