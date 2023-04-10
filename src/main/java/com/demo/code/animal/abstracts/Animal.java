package com.demo.code.animal.abstracts;

public abstract class Animal implements Eatable {

    public abstract void eat();

    @Override
    public void eat(Food food) {
        System.out.println("Animal eat food: " + food.getName());
    }
}
