package com.learn.code.minioSpring.com.minis.event;

import java.util.EventObject;

/**
 * 应用监听
 */
public class ApplicationEvent extends EventObject {
    private static final long serialVersionUID = 1L;

    protected String msg = null;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationEvent(Object source) {
        super(source);
        msg = source.toString();
    }
}
