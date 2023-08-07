package com.demo.code.generate;


/**
 * 通过这个demo 可以知道，就是普通父类需要实现所有的接口方法，而子类就不要需要实现了
 * 但是也可以重写。
 */
public class Dog extends Animal {

    @Override
    public void eat(Food food) {
        System.out.println("Dog eat food: " + food.getName());
    }

    @Override
    public void eat(Food food, String name) {
        System.out.println("Dog eat food: " + food.getName() + " name: " + name);
    }

}
