package com.example.template.util;


import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 简化spring提供的jdbcTemplate相关方法，提供更简单的使用方法
 */
@Component
public class EasyJdbc<T> {

    /**
     * 插入语句模板
     */
    public static final String INSERT_STATEMENT = "INSERT INTO %s(%s) VALUES(%s)";
    public static final String SELECT_STATEMENT = "SELECT * FROM %s WHERE ID = ?";
    public static final boolean ID_AUTO_INCREMENT = true;
    /**
     * 批量插入最大行数
     */
    public static final int BATCH_SIZE = 2000;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    /**
     * 生成namedParameterJdbcTemplate需要的insert语句
     *
     * @param obj 对象
     * @return namedParameterJdbcTemplate需要的insert语句
     */
    public String getInsertSql(T obj) {
        Class<?> clazz = obj.getClass();
        if (clazz == Object.class) {
            print("传入对象不能是Object");
            return "";
        }
        List<Class<?>> classes = getSuperClasses(clazz);
        String tableName = getTableName(clazz);
        if (isEmpty(tableName)) {
            throw new RuntimeException("实体类上面没有找到表名");
        }
        List<String> fields = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (Class<?> aClass : classes) {
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field field : declaredFields) {
                // 只处理实例变量
                if (isStatic(field)) {
                    continue;
                }
                // 不处理Transient，数据库不存在该字段
                Transient aTransient = field.getAnnotation(Transient.class);
                if (Objects.nonNull(aTransient)) {
                    continue;
                }
                String fieldName = field.getName();
                String getMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Object invoke = null;
                try {
                    Method method = clazz.getMethod(getMethod);
                    invoke = method.invoke(obj);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    print(e.getMessage());
                }
                // 过滤null
                if (Objects.isNull(invoke)) {
                    continue;
                }
                String valueName = fieldName;
                Column column = field.getAnnotation(Column.class);
                // 查看注解上是否有值
                if (Objects.nonNull(column)) {
                    fieldName = column.name();
                }
                fields.add(fieldName);
                values.add(":" + valueName);
            }
        }
        String fieldsWithComma = String.join(",", fields);
        String valuesWithComma = String.join(",", values);
        return String.format(INSERT_STATEMENT, tableName, fieldsWithComma, valuesWithComma);
    }

    private String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        return Optional.ofNullable(table)
                .map(Table::name).orElse("");
    }

    /**
     * 批量插入
     *
     * @param list 插入对象列表
     * @return 插入行数
     */
    public boolean batchSave(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            print("批量插入列表为空");
            return false;
        }
        String insert = getInsertSql(list.get(0));
        if (isEmpty(insert)) {
            return false;
        }
        if (list.size() > BATCH_SIZE) {
            // 分批次插入，缓解数据库压力
            List<List<T>> partitions = ListUtils.partition(list, BATCH_SIZE);
            for (List<T> partition : partitions) {
                SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(partition);
                parameterJdbcTemplate.batchUpdate(insert, batch);
            }
        } else {
            SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(list);
            parameterJdbcTemplate.batchUpdate(insert, batch);
        }
        return true;
    }

    /**
     * 插入对象到数据库
     *
     * @param t 插入对象
     * @return 主键，如果非自增主键返回null
     */
    public Number insertSelective(T t) {
        if (Objects.isNull(t)) {
            throw new RuntimeException("插入对象不能为空");
        }
        String insertSql = getInsertSql(t);
        SqlParameterSource source = new BeanPropertySqlParameterSource(t);
        if (ID_AUTO_INCREMENT) {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            parameterJdbcTemplate.update(insertSql, source, keyHolder);
            return keyHolder.getKey();
        } else {
            parameterJdbcTemplate.update(insertSql, source);
            return null;
        }
    }


    /**
     * 通过主键查找记录
     *
     * @param id          主键
     * @param clazz       业务实体类
     * @param resultClazz 返回对象
     * @param <R>         返回类泛型
     * @return 主键对应的记录
     */
    public <R> R selectByPrimaryKey(String id, Class<T> clazz, Class<R> resultClazz) {
        String tableName = getTableName(clazz);
        if (isEmpty(tableName)) {
            throw new RuntimeException("实体类上面没有找到表名");
        }
        return jdbcTemplate.queryForObject(String.format(SELECT_STATEMENT, tableName),
                BeanPropertyRowMapper.newInstance(resultClazz), id);
    }

    /**
     * 根据自定义的sql语句查询列表
     *
     * @param sql         自定义SQL语句
     * @param source      sql参数
     * @param resultClazz 返回对象
     * @param <R>         返回类型泛型
     * @return 列表
     */
    public <R> List<R> queryByCustomSql(String sql, SqlParameterSource source, Class<R> resultClazz) {
        return parameterJdbcTemplate.query(sql, source, BeanPropertyRowMapper.newInstance(resultClazz));
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 判断该字段是否是静态变量
     *
     * @param field 字段
     * @return true：静态变量
     */
    private static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    private static void print(String message) {
        System.out.println(message);
    }


    /**
     * 递归查找父类
     *
     * @param clazz 当前类
     * @return 当前类和父类的集合
     */
    private static List<Class<?>> getSuperClasses(Class<?> clazz) {
        List<Class<?>> classes = new ArrayList<>();
        classes.add(clazz);
        Class<?> temp = clazz;
        while (temp != null) {
            if (temp == Object.class) {
                break;
            }
            Class<?> superclass = temp.getSuperclass();
            classes.add(superclass);
            temp = superclass;
        }
        return classes;
    }

}
