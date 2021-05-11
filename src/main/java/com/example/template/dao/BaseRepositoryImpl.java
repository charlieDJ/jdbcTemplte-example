package com.example.template.dao;

import com.example.template.util.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author dj
 * @date 2021/5/11
 */
@Slf4j
public class BaseRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    /**
     * 批量插入最大行数
     */
    public static final int BATCH_SIZE = 2000;
    /**
     * 插入语句模板
     */
    public static final String INSERT_STATEMENT = "INSERT INTO %s(%s) VALUES(%s)";

    private final EntityManager entityManager;
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    public BaseRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        // Keep the EntityManager around to used from the newly introduced methods.
        this.entityManager = entityManager;
    }

    @Override
    public boolean saveBatch(List<T> list) {
        parameterJdbcTemplate = SpringHolder.getBean(NamedParameterJdbcTemplate.class);
        if (CollectionUtils.isEmpty(list)) {
            log.info("批量插入列表为空");
            return false;
        }
        String insert = getInsertSql(list.get(0));
        log.info("批量插入SQL语句：{}", insert);
        if (StringUtils.isEmpty(insert)) {
            return false;
        }
        if (list.size() > BATCH_SIZE) {
            // 分批次插入
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
     * 生成namedParameterJdbcTemplate需要的insert语句
     *
     * @param obj 对象
     * @return namedParameterJdbcTemplate需要的insert语句
     */
    public String getInsertSql(T obj) {
        Class<?> clazz = obj.getClass();
        if (clazz == Object.class) {
            log.info("传入对象不能是Object");
            return "";
        }
        List<Class<?>> classes = getSuperClasses(clazz);
        String tableName = getTableName(clazz);
        if (StringUtils.isEmpty(tableName)) {
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
                    log.error(e.getMessage());
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
     * 判断该字段是否是静态变量
     *
     * @param field 字段
     * @return true：静态变量
     */
    private static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
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
