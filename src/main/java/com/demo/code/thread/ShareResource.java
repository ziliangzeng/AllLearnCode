package com.demo.code.thread;

public class ShareResource {
    private int count = 0;

    public synchronized void inc() {
        count++;
    }

    public synchronized void dec() {
        count--;
    }

    public synchronized int getCount() {
        return count;
    }



}
