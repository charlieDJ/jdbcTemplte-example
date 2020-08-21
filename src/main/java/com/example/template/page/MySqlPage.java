package com.example.template.page;

import java.text.MessageFormat;

public class MySqlPage extends Page {

    public static final String PAGE_SQL = " limit {0},{1} ";


    @Override
    public String startPage(String sql, int page, int size) {
        Pagination pagination = new Pagination.Builder().setPage(page).setSize(size).build();
        int beginIndex = (pagination.getPage() - 1) * pagination.getSize();
        return MessageFormat.format(sql + PAGE_SQL, beginIndex, pagination.getSize());
    }

}
