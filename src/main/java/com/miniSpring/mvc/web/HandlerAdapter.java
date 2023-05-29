package com.miniSpring.mvc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Adapter 适配器
 */
public interface HandlerAdapter {
    void handle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception;
}
