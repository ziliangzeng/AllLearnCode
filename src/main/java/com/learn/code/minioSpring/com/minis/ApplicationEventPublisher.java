package com.learn.code.minioSpring.com.minis;

import org.springframework.context.ApplicationEvent;

/**
 * 事件发布者
 */
public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}
