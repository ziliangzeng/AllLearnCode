package com.demo.code.animal.global;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                try {
                    throw new RuntimeException("i = 5");

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println(i);
        }

    }
}
