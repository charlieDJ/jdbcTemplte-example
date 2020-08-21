package com.example.template.page;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PageFactory {

    private static final Map<Class<?>, Page> map = new HashMap<>();

    static {
        map.put(MySqlPage.class, new MySqlPage());
    }

    public static Page getPage(Class<?> sql) {
        Page page = map.get(sql);
        if (Objects.isNull(page)) {
            throw new RuntimeException("未查找到对应分页处理类");
        }
        Page page1;
        try {
            page1 = (Page) sql.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("未查找到对应分页处理类");
        }
        return page1;
    }
}
