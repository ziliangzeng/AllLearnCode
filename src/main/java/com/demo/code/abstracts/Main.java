package com.demo.code.abstracts;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        String test = "test//test/test";

        int i = nthIndexOf(test, "/", 2);
        System.out.println(i);

    }


    public static int nthIndexOf(String text, String target, int n) {
        int index = text.indexOf(target);
        while (index >= 0 && --n > 0) {
            index = text.indexOf(target, index + 1);
        }
        return index;
    }

}
