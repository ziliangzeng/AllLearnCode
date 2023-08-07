package com.demo.code.proxy;

public class ProxyInterfaceImpl implements ProxyInterface {
    @Override
    public void doSomething() {
        System.out.println("测试代理是否有作用");
    }

//    @Override
//    public void doSomething2() {
//        System.out.println("测试代理是否有作用2");
//    }
}
