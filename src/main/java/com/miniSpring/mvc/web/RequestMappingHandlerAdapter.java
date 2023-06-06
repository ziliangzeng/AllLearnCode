package com.miniSpring.mvc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestMappingHandlerAdapter implements HandlerAdapter{

    /**
     * TODO 作用在哪里
     */
    WebApplicationContext wac;

    public RequestMappingHandlerAdapter(WebApplicationContext wac) {
        this.wac = wac;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        handleInternal(request,response,(HandlerMethod)handler);
    }

    private void handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        //TODO
        Method method = handler.getMethod();
        Object bean = handler.getBean();
        Object objResult = null;
        try{
            objResult = method.invoke(bean);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            response.getWriter().append(objResult.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
