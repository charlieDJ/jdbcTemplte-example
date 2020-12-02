package com.example.template.page;

public class PageFactory {

    public static Page newInstance(Class<? extends Page> sql) {
        try {
            return sql.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("未查找到对应分页处理类");
        }
    }

}
