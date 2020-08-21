package com.example.template;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterTest {

    @Test
    public void whenFilterListWithCombinedPredicatesUsingAnd_thenSuccess() {
        Predicate<String> predicate1 = str -> str.startsWith("A");
        Predicate<String> predicate2 = str -> str.length() < 5;
        List<String> names = Arrays.asList("Adam", "Alexander", "John", "Tom");
        List<String> result = names.stream()
                .filter(predicate1.and(predicate2))
                .collect(Collectors.toList());

        assertEquals(1, result.size());
    }

    @Test
    public void equals(){
        String a = "abc";
        System.out.println("abc,".equals(a));
    }

}
