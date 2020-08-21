package com.example.template.page;

import org.springframework.util.StringUtils;

import java.text.MessageFormat;

public class SqlBuilder {

    private String sql;
    private String countSql;


    public String getSql() {
        System.out.println("sql: " + sql);
        return this.sql;
    }

    public String getCountSql() {
        System.out.println("count: " + countSql);
        return countSql;
    }

    public SqlBuilder(Builder builder) {
        this.sql = builder.sql;
        this.countSql = builder.countSql;
    }

    public static class Builder {
        private static final String BEFORE_FROM_SQL = " SELECT {0} FROM ";
        private static final String COUNT_SQL = " SELECT COUNT(1) FROM ";
        private String columns;
        private String afterFromSql;
        private String sql;
        private String countSql;

        public Builder setColumns(String columns) {
            this.columns = columns;
            return this;
        }

        public Builder setAfterFromSql(String afterFromSql) {
            this.afterFromSql = afterFromSql;
            return this;
        }

        public SqlBuilder build() {
            if (StringUtils.isEmpty(columns)) {
                throw new RuntimeException("没有传入查询的字段");
            }
            if ("*".equals(columns.trim())) {
                System.out.println("查询字段尽量不要使用*");
            }

            if (StringUtils.isEmpty(afterFromSql)) {
                throw new RuntimeException("没有传入表名及where语句");
            }

            this.sql = MessageFormat.format(BEFORE_FROM_SQL, columns) + afterFromSql;

            this.countSql = COUNT_SQL + afterFromSql;

            return new SqlBuilder(this);
        }
    }

}
