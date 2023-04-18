package com.demo.code.animal.generate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.eat();
        dog.eat(new Food("food1", "type1"), "name");
        dog.eat(new Food("food2", "type2"));
    }

}
