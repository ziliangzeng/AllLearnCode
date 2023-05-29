package com.demo.code.animal.global;

import java.util.ArrayList;
import java.util.List;

public class A {

    //实例变量(实例内共享)
    private String a = new String();
    //类变量(实例共享)
    private static List<String> b = new ArrayList<>();


    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public List<String> getB() {
        return b;
    }

    public void setB(List<String> b) {
        this.b = b;
    }
}
