package com.demo.code.animal.generate;

public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.eat();
        dog.eat(new Food("food1", "type1"), "name");
        dog.eat(new Food("food2", "type2"));
    }
}
