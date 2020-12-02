package com.example.template.page;

import java.text.MessageFormat;

public class OraclePage extends Page {


    public static final String PAGE_SQL = " SELECT B.* FROM (SELECT A.*,ROWNUM rn FROM ({0}) A WHERE ROWNUM <={1}) B WHERE B.rn >={2} ";

    @Override
    public String startPage(String sql, int page, int size) {
        Pagination pagination = new Pagination.Builder().setPage(page).setSize(size).build();
        int beginIndex = (pagination.getPage() - 1) * pagination.getSize();
        return MessageFormat.format(PAGE_SQL, sql, beginIndex, pagination.getSize());
    }
}
