package com.learn.code.entity;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;

/**
 * @Auther: qsx00
 * @Date: 2023/3/8 22:04
 * @Description:
 */
public class User {

    private String name;

    private int age;

    @ManagedAttribute
    public String getName() {
        return name;
    }

    @ManagedOperation
    @ManagedOperationParameter(name = "name", description = "name")
    public void setName(String name) {
        this.name = name;
    }

    @ManagedAttribute
    public int getAge() {
        return age;
    }

    @ManagedOperation
    @ManagedOperationParameter(name = "age", description = "age")
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    @ManagedOperation
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
