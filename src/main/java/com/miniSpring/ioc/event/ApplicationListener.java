package com.miniSpring.ioc.event;

import java.util.EventListener;

public class ApplicationListener implements EventListener {

    void onApplicationEvent(ApplicationEvent event) {
        System.out.println("onApplicationEvent: " + event.toString());
    }
}
