package com.example.template.function;

import java.util.Objects;

public class FunctionTest {
    public static void main(String[] args) {
        Integer wrap = SupplierWrapper.wrap(() -> 1 + 1);
        System.out.println("Objects.isNull(wrap) = " + Objects.isNull(wrap));
    }
}
