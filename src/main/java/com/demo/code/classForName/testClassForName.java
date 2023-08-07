package com.demo.code.classForName;

public class testClassForName {
    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> aClass = Class.forName("com.demo.code.classForName.ClassForNameBean");
    }

}
