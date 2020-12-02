package com.example.template;

import com.example.template.page.OraclePage;
import org.springframework.core.ResolvableType;

public class ResolveTest {

    public static void main(String[] args) {
        ResolvableType resolvableType = ResolvableType.forClass(OraclePage.class);
        System.out.println(resolvableType.getSuperType().getType());
    }
}
