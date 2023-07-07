package com.demo.code.animal.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CgLibTestProxy {
    public static void main(String[] args) {
        ProxyInterfaceImpl proxyInterface = new ProxyInterfaceImpl();

        CGLibProxy cgLibProxy = new CGLibProxy(proxyInterface);

        //不支持动态实现接口
        Object o = Enhancer.create(ProxyInterfaceImpl.class, new Class[]{ProxyInterface3.class}, cgLibProxy);

//        o.doSomething();
//        o.doSomething2();
//        o1.doSomething3();


    }


}

class CGLibProxy implements MethodInterceptor {

    private Object target;

    public CGLibProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println("Cglib proxy");
        Object invoke = methodProxy.invoke(target, objects);
        return invoke;
    }
}
