package com.demo.code.threadLocal;

public class Main {

    public static void main(String[] args) {
        /**
         * ThreadLocal 第一次创建的时候通过Thread.currentThread().threadLocals = new ThreadLocalMap(this, firstValue);
         *
         * 然后一个线程可以有多个ThreadLocal，所以ThreadLocalMap是一个数组，ThreadLocalMap.Entry[] table
         *
         * ThreadLocalMap.Entry 是一个静态内部类，里面有一个ThreadLocal的弱引用，一个value
         *
         * 通过当前ThreadLocal的hash值，对数组长度取模，得到一个下标，然后将ThreadLocalMap.Entry放到数组中
         * 如果hash冲突了 那么就是i+1的操作
         *
         */
        ThreadLocal<String> tl0 = new ThreadLocal<>();
        ThreadLocal<String> tl1 = new ThreadLocal<>();
        ThreadLocal<String> tl2 = new ThreadLocal<>();

        tl0.set("tl0");
        tl1.set("tl1");
        tl2.set("tl2");

        System.out.println(tl0.get());

    }




}
