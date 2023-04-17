package com.demo.code.animal.classForName;

public class ClassForNameBean {

    static {
        System.out.println("静态代码块");
    }

    public ClassForNameBean() {
        System.out.println("构造方法");
    }


    public static void codeMethod(){
        System.out.println("静态方法");
    }

}
