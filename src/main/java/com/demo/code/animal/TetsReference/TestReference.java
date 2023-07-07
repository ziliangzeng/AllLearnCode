package com.demo.code.animal.TetsReference;

import java.util.ArrayList;

public class TestReference {

    public static void main(String[] args) {

        // = 都是引用传递!?

        ArrayList<Integer> a = new ArrayList<>();

        a.add(1);
        a.add(2);
        a.add(3);

        System.out.println(a);
        ArrayList<Integer> b = a;
        b.add(4);
        System.out.println(b);
        System.out.println(a);


        Integer[] c = {1, 2, 3};
        for (int i = 0; i < c.length; i++) {
            System.out.println(c[i]);
        }

        Integer[] d = c;

        d[0] = 4;

        for (int i = 0; i < c.length; i++) {
            System.out.println(c[i]);
        }

        String e = "1234";

        String f = e;

        f = "123";

        System.out.println(e);
        System.out.println(f);


    }
}
