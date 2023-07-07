package com.demo.code.animal.proxy;

import java.lang.reflect.Proxy;

public class TestProxy {

    public static void main(String[] args) {

        ProxyInterfaceImpl proxyInterface = new ProxyInterfaceImpl();

        ProxyInterface2 proxy1 = (ProxyInterface2) Proxy.newProxyInstance(proxyInterface.getClass().getClassLoader(), new Class[]{ProxyInterface.class,ProxyInterface2.class}, (proxy, method, args1) -> {

            System.out.println("doSomething22222222222");

            System.out.println("proxy");

            //TODO 像mybatis 这种，就是传入的method名，那么就可以去看xml的写好的sql语句，然后进行映射匹配
            System.out.println(method.getName());


            return null;
        });
        proxy1.doSomething2();
    }
}
