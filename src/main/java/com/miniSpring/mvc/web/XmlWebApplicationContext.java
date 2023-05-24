package com.miniSpring.mvc.web;

import com.miniSpring.ioc.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;

/**
 * 替换一开始AnnotationConfigWebApplicationContext
 */
public class XmlWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext {

    private ServletContext servletContext;

    public XmlWebApplicationContext(String configLocation) {
        super(configLocation);
    }


    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
