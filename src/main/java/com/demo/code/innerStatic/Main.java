package com.demo.code.innerStatic;

public class Main {

    public static void main(String[] args) {

        Thread thread0 = new Thread(() -> {

            AllClass allClass0 = new AllClass("allClass0", new AllClass.Inner("inner0"));

            try {
                Thread.sleep(100);
                String name = allClass0.getInner().getName();
                System.out.println("thread0: " + name);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
        Thread thread1 = new Thread(() -> {

            AllClass allClass1 = new AllClass("allClass1", new AllClass.Inner("inner1"));

            try {
                Thread.sleep(100);
                String name = allClass1.getInner().getName();
                System.out.println("thread1: " + name);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });

        Thread thread2 = new Thread(() -> {
            AllClass allClass2 = new AllClass("allClass0", new AllClass.Inner("inner2"));
            try {
                Thread.sleep(100);
                String name = allClass2.getInner().getName();
                System.out.println("thread2: " + name);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread0.start();
        thread1.start();
        thread2.start();







    }


}
