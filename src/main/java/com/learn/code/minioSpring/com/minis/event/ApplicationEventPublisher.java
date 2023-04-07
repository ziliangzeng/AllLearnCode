package com.learn.code.minioSpring.com.minis.event;


/**
 * 事件发布者
 */
public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
    void addApplicationListener(ApplicationListener listener);
}
