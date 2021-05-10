package com.example.template.config;

import com.example.template.dao.JpaDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import javax.annotation.PostConstruct;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * @author dj
 * @date 2021/5/8
 */
@Slf4j
@Configuration
public class JpaConverterConfiguration {
    @Autowired
    private ApplicationContext applicationContext;
    /**
     * Initialize injection @JpaDto Corresponding Converter
     */
    @PostConstruct
    public void init() {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(JpaDto.class);
        for (Object o : map.values()) {
            Class c = o.getClass();
            log.info("Jpa add to Converter,class={}", c.getName());
            GenericConversionService genericConversionService = ((GenericConversionService) DefaultConversionService.getSharedInstance());
            genericConversionService.addConverter(Map.class, c, m -> {
                try {
                    Object obj = c.newInstance();
                    return copyMapToObj(m, obj);
                } catch (Exception e) {
                    throw new FatalBeanException("Jpa Result conversion error ,class=" + c.getName(), e);
                }
            });
        }
    }

    /**
     * take map The value in copy To bean On the corresponding field in
     *
     * @param map
     * @param target
     * @return
     * @author bazhandao
     * @date 2020-03-26
     */
    private Object copyMapToObj(Map<String, Object> map, Object target) {
        if (map == null || target == null || map.isEmpty()) {
            return target;
        }
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() == null) {
                continue;
            }
            try {
                String key = targetPd.getName();
                Object value = map.get(key);
                if (value == null) {
                    value = map.get(key.toUpperCase());
                }
                if (value == null) {
                    value = map.get(toUnderlineCase(key));
                }
                if (value == null) {
                    continue;
                }
                Method writeMethod = targetPd.getWriteMethod();
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                writeMethod.invoke(target, value);
            } catch (Exception ex) {
                log.debug("Could not copy properties from source to target", ex);
//                throw new FatalBeanException("Could not copy properties from source to target", ex);
            }
        }
        return target;
    }

    /**
     * 下划线转驼峰
     * user_name  ---->  userName
     * userName   --->  userName
     *
     * @param underlineStr 带有下划线的字符串
     * @return 驼峰字符串
     */
    private String toCamelCase(String underlineStr) {
        if (underlineStr == null) {
            return null;
        }
        // 分成数组
        char[] charArray = underlineStr.toCharArray();
        // 判断上次循环的字符是否是"_"
        boolean underlineBefore = false;
        StringBuilder builder = new StringBuilder();
        for (int i = 0, l = charArray.length; i < l; i++) {
            // 判断当前字符是否是"_",如果跳出本次循环
            if (charArray[i] == 95) {
                underlineBefore = true;
            } else if (underlineBefore) {
                // 如果为true，代表上次的字符是"_",当前字符需要转成大写
                builder.append(charArray[i] -= 32);
                underlineBefore = false;
            } else {
                // 不是"_"后的字符就直接追加
                builder.append(charArray[i]);
            }
        }
        return builder.toString();
    }

    /**
     * 驼峰转 下划线
     * userName  ---->  user_name
     * user_name  ---->  user_name
     *
     * @param camelCaseStr 驼峰字符串
     * @return 带下滑线的String
     */
    public static String toUnderlineCase(String camelCaseStr) {
        if (camelCaseStr == null) {
            return null;
        }
        // 将驼峰字符串转换成数组
        char[] charArray = camelCaseStr.toCharArray();
        StringBuffer buffer = new StringBuffer();
        //处理字符串
        for (int i = 0, l = charArray.length; i < l; i++) {
            if (charArray[i] >= 65 && charArray[i] <= 90) {
                buffer.append("_").append(charArray[i] += 32);
            } else {
                buffer.append(charArray[i]);
            }
        }
        return buffer.toString();
    }
}
