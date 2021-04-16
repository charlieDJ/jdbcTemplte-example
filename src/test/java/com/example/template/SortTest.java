package com.example.template;

import com.example.template.model.MyVo;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * @author dj
 * @date 2021/1/28
 */
public class SortTest {

    @Test
    public void sort() {
        Stream.of(new MyVo("map"), new MyVo("set"), new MyVo(null))
                .sorted(Comparator.comparing(MyVo::getName, Comparator.nullsFirst(Comparator.naturalOrder())))
                .forEach(e -> System.out.println(e.getName()));

    }

}
