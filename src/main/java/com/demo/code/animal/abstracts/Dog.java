package com.demo.code.animal.abstracts;


/**
 * 通过这个demo 可以知道，就是抽象方法实现了，但是抽象类没有实现，那么子类就必须实现
 */
public class Dog extends Animal{
    @Override
    public void eat() {
        System.out.println("Dog eat food");
    }

    @Override
    public void eat(Food food, String name) {
        System.out.println("Dog eat food: " + food.getName() + " name: " + name);
    }
}
