package com.example.template.entity;

import com.example.template.config.AuditListener;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author dj
 * @date 2021/1/26
 */
@Entity
@Data
@ToString
@Accessors(chain = true)
@Table(name = "EMPLOYEE")
@EntityListeners(AuditListener.class)
public class Employee implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    @Column(name = "dept_id")
    private Integer deptId;

    @Embedded
    private Audit audit;

    @Override
    public Audit getAudit() {
        return audit;
    }

    @Override
    public void setAudit(Audit audit) {
        this.audit = audit;
    }
}
