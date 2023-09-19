package com.demo.code.thread;

public class Main {
    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource();
        MyThread thread0 = new MyThread(shareResource);
        MyThread thread1 = new MyThread(shareResource);
        thread0.start();
        thread1.start();

//        Thread thread0 = new Thread(new MyRunnable(shareResource));
//        Thread thread1 = new Thread(new MyRunnable(shareResource));
//
//        thread0.start();
//        thread1.start();
//

        try {
            thread0.join();
            thread1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("shareResource:" + shareResource.getCount());


    }
}
