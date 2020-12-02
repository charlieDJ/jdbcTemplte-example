package com.example.template.util;

import java.util.function.UnaryOperator;

public class RecursiveLambdaExample {
    static UnaryOperator<Integer> fac = i -> i == 0 ? 1 : i * RecursiveLambdaExample.fac.apply( i - 1);

    UnaryOperator<Integer> f = i -> i == 0 ? 1 : i * this.f.apply( i - 1);

    public static void main(String[] args) {
        System.out.println(new RecursiveLambdaExample().f.apply(5));
//        System.out.println(fac.apply(5));
    }
}
