package com.demo.code.thread;

public class MyRunnable implements Runnable {


    private ShareResource shareResource;

    public MyRunnable(ShareResource shareResource) {
        this.shareResource = shareResource;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            shareResource.inc();
        }
    }
}
