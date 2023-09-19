package com.demo.code.threadPool;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        //

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService);


        for (int i = 0; i < 10; i++) {

            final int currentLockIndex = i;
            completionService.submit(()->{

                System.out.println("当前线程index:" + currentLockIndex);

                TimeUnit.MINUTES.sleep(2);

                return currentLockIndex;
            });

        }


    }
}
