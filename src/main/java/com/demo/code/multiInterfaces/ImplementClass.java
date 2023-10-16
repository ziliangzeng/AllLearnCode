package com.demo.code.multiInterfaces;

public class ImplementClass implements InterfaceA, InterfaceB {


    @Override
    public void doSomethingA() {
        System.out.println("doSomethingA");
    }

    @Override
    public void doSomethingB() {

        System.out.println("doSomethingB");

    }
}
