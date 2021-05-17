package com.example.template;

import com.example.template.entity.TestJoinColumn;
import com.example.template.util.EasyJdbc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author dj
 * @date 2021/5/13
 */
public class JoinColumnTest extends TemplateApplicationTests  {

    @Autowired
    private EasyJdbc easyJdbc;

    @Test
    public void getSql(){
        TestJoinColumn column = new TestJoinColumn();
        column.setId(1)
                .setPersonId("2");
        System.out.println(easyJdbc.getInsertSql(column));
    }

}
