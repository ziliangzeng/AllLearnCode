package com.demo.code.DateCompable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        ArrayList<String> strings = new ArrayList<>();


        strings.add("1");

        String collect = strings.stream().collect(Collectors.joining(","));
        System.out.println(collect);
    }



}
