package com.demo.code.multiInterfaces;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Main {

    public static void main(String[] args) {

        ImplementClass implementClass = new ImplementClass();

        InterfaceA doSomethingA = (InterfaceA) Proxy.newProxyInstance(Main.class.getClassLoader(), implementClass.getClass().getInterfaces(),
                (proxy, method, args1) -> {
                    System.out.println("测试");
                    return method.invoke(implementClass, args1);
                }


        );

        doSomethingA.doSomethingA();
//        doSomethingA.doSomethingB();

    }
}
