package com.learn.code.minioSpring.com.minis.event;

import com.learn.code.minioSpring.com.minis.event.ApplicationEvent;

import java.util.EventListener;

public class ApplicationListener implements EventListener {

    void onApplicationEvent(ApplicationEvent event) {
        System.out.println("onApplicationEvent: " + event.toString());
    }
}
