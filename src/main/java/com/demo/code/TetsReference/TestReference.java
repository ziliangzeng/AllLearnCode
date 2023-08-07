package com.demo.code.TetsReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestReference {

    public static void main(String[] args) {

        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        System.out.println(integers);
        change(integers);
        System.out.println(integers);


    }

    private static void change(ArrayList<Integer> integers) {
        ArrayList<Integer> integers1 = new ArrayList<>();

        integers.add(3);
        integers.add(3);

        integers = integers1;

    }
}
