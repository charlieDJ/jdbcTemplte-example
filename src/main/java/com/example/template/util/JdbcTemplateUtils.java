package com.example.template.util;


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

public class JdbcTemplateUtils {

    public static final String INSERT_STATEMENT = "INSERT INTO %s(%s) VALUES(%s)";

    /**
     * 生成namedParameterJdbcTemplate需要的insert语句
     *
     * @param obj 对象
     * @param <T> 泛型
     * @return namedParameterJdbcTemplate需要的insert语句
     */
    public static <T> String generateInsert(T obj) {
        Class<?> clazz = obj.getClass();
        if (clazz == Object.class) {
            print("传入对象不能是Object");
            return "";
        }
        List<Class<?>> classes = getSuperClasses(clazz);
        Table table = clazz.getAnnotation(Table.class);
        String tableName = Optional.ofNullable(table)
                .map(Table::name).orElse("");
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
        Class temp = clazz;
        while (temp != null) {
            if (temp == Object.class) {
                break;
            }
            Class superclass = temp.getSuperclass();
            classes.add(superclass);
            temp = superclass;
        }
        return classes;
    }

}
