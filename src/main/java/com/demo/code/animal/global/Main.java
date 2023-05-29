package com.demo.code.animal.global;

public class Main {
    public static void main(String[] args) {
        A a = new A();
        a.setA("a");
        a.getB().add("b");
        System.out.println(a.getA());
        System.out.println(a.getB());

        A a1 = new A();
        System.out.println(a1.getA());
        System.out.println(a1.getB());

    }
}
