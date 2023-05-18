package com.miniSpring.mvc.web;

import com.miniSpring.ioc.ApplicationContext;

import javax.servlet.ServletContext;

/**
 * 还需要一个实现类
 * 如果不出意外的话，那么会自动注入这个DefaultListableBeanFactory
 */
public interface WebApplicationContext extends ApplicationContext {

    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";

    ServletContext getServletContext();

    void setServletContext(ServletContext servletContext);

}
