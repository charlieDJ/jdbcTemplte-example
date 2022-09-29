package com.example.template;

import com.example.template.model.EmployeeVo;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author dj
 * @date 2021/5/24
 */
@Slf4j
public class EntityManagerTest extends TemplateApplicationTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    @Test
    public void query(){
        List<EmployeeVo> list = entityManager.createNativeQuery("select firstName,lastName from EMPLOYEE where id = 11")
                .unwrap(NativeQueryImpl.class)
//                .addScalar("firstName", StandardBasicTypes.STRING)
//                .addScalar("lastName", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(EmployeeVo.class))
                .getResultList();
        log.info(String.valueOf(list.get(0)));
    }

}
