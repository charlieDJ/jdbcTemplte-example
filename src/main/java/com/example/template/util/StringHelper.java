package com.example.template.util;

import java.util.Objects;

public class StringHelper {

    private String reverse(String arg) {
        if (Objects.isNull(arg)) {
            return null;
        }
        if (arg.length() == 0) {
            return arg;
        } else {
            return reverse(arg.substring(1)) + arg.substring(0, 1);
        }
    }

    public static void main(String[] args) {
        String arg = "main";
        StringHelper helper = new StringHelper();
        String reverse = helper.reverse(arg);
        System.out.println("文字反向：" + reverse);
    }

}
