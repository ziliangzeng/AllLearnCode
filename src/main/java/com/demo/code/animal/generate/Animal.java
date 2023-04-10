package com.demo.code.animal.generate;

public class Animal implements Eatable {

    public void eat(){
        System.out.println("Animal eat");
    }

    @Override
    public void eat(Food food) {
        System.out.println("Animal eat food: " + food.getName());
    }

    @Override
    public void eat(Food food, String name) {
        System.out.println("Animal eat food: " + food.getName() + " name: " + name);
    }
}
