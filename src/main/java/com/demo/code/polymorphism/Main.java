package com.demo.code.polymorphism;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

//        Father father = new Son("cheshi");
//        father.getFatherNamess();
//
//
//        Son son = (Son) father;
//        son.getSonNamess();
//        son.getFatherNamess();
//
//        System.out.println(son.getSonName());


        HashMap<String, Integer> map = new HashMap<>();
        map.put("1",3);
        map.put("2",123);

        Integer remove = map.remove("1");

        System.out.println(remove);


    }
}
