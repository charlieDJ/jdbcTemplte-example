package com.example.template;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatMapTest {

    @Test
    public void flatMapTest(){
        String[][] data = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};
        Stream<String[]> temp = Arrays.stream(data);

        Stream<String> stringStream = temp.flatMap(Arrays::stream);
        List<String> list = stringStream.filter("a"::equals).collect(Collectors.toList());
        list.forEach(System.out::println);
    }

}
