package com.demo.code.thread;

public class MyThread extends Thread {


    private ShareResource shareResource;


    public MyThread(ShareResource shareResource) {
        this.shareResource = shareResource;
    }


    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            shareResource.inc();
        }
    }
}
