package com.miniSpring.mvc.web;

import com.miniSpring.ioc.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;

/**
 * 居然是直接继承了ClassPathXmlApplicationContext 实现了，是我狭隘了哈哈哈
 */
public class AnnotationConfigWebApplicationContext extends ClassPathXmlApplicationContext
implements WebApplicationContext{

    private ServletContext servletContext;

    public AnnotationConfigWebApplicationContext(String fileName) {
        super(fileName);
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
          this.servletContext = servletContext;
    }
}
