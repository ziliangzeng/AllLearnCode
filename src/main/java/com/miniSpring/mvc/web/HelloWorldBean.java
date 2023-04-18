package com.miniSpring.mvc.web;

public class HelloWorldBean {

    public void doGet(){
        System.out.println("doGet hello world");
    }

    public String doPost(){
        return "doPost hello world";
    }

}
