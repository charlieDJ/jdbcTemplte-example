package com.example.template.page;

import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SqlBuilder {

    private final String sql;
    private final String countSql;


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

    public static Builder newInstance() {
        return new Builder();
    }

    public static class Builder {
        private static final String STANDARD_SQL = " SELECT {0} FROM ";
        private static final String COUNT_SQL = " SELECT COUNT(1) FROM ";
        private String columns;
        private String afterFromSql;
        private String sql;
        private String countSql;
        private final List<String> conditions = new ArrayList<>();

        public Builder setColumns(String columns) {
            if (this.columns == null) {
                this.columns = columns;
            } else {
                this.columns = this.columns + "," + columns;
            }
            return this;
        }

        public Builder setColumns(List<String> columns) {
            String newColumns = columns.stream().distinct()
                    .collect(Collectors.joining(","));
            if (this.columns == null) {
                this.columns = newColumns;
            } else {
                this.columns = this.columns + "," + newColumns;
            }
            return this;
        }

        public Builder where(String condition) {
            conditions.add(condition);
            return this;
        }

        public Builder setAfterFromSql(String afterFromSql) {
            this.afterFromSql = " " + afterFromSql + " ";
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
                throw new RuntimeException("表名或表连接不能为空");
            }

            String whereClause = "";
            if (!conditions.isEmpty()) {
                whereClause = conditions.stream().collect(Collectors.joining(" AND ", " WHERE ", ""));
            }

            this.sql = MessageFormat.format(STANDARD_SQL, columns) + afterFromSql +  whereClause;

            this.countSql = COUNT_SQL +  afterFromSql  + whereClause;

            return new SqlBuilder(this);
        }
    }

}
