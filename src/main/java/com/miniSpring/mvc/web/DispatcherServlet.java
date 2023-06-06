package com.miniSpring.mvc.web;


import com.miniSpring.ioc.BeansException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根本性需要Servlet支持
 * 然后就是url路径和方法的映射需要存储
 * 然后再在业务层使用反射调用方法的形式执行方法
 * <p>
 * <p>
 * <p>
 * <p>
 * SpringMVC的容器直接管理跟DispatcherServlet相关的Bean，也就是Controller，ViewResolver等，并且SpringMVC容器是在DispacherServlet的init方法里创建的。
 * 而Spring容器管理其他的Bean比如Service和DAO。
 * 并且SpringMVC容器是Spring容器的子容器，
 * 所谓的父子关系意味着什么呢，就是你通过子容器去拿某个Bean时，子容器先在自己管理的Bean中去找这个Bean，如果找不到再到父容器中找。但是父容器不能到子容器中去找某个Bean。
 */
public class DispatcherServlet extends HttpServlet {

    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = "WEB_APPLICATION_CONTEXT_ATTRIBUTE";

    private WebApplicationContext webApplicationContext;
    private WebApplicationContext parentApplicationContext;


    private RequestMappingHandlerMapping handlerMapping;
    private RequestMappingHandlerAdapter handlerAdapter;


    private String sContextConfigLocation;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        /**
         * 这里对应上了web.xml中的配置
         *
         *         <init-param>
         *             <param-name>contextConfigLocation</param-name>
         *             <!-- 初始化配置文件地址，所有的配置参数都由这里引入 -->
         *             <param-value>/WEB-INF/minisMVC-servlet.xml</param-value>
         *         </init-param>
         *
         *         这里的getInitParameter 跟ServletContext.getInitParameter的又不同哦
         *
         *
         */

        /**
         *
         * //设置IOC容器对象
         * 在listener里面引入IOC容器
         *
         */
        this.parentApplicationContext = (WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;

        try {
            //拿到xml配置地址
            xmlPath = this.getServletContext().getResource(sContextConfigLocation);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        //资源加载和解析


        /**
         * 构造器这里把 sContextConfigLocation -> beanDefinitionMap 传进去了，
         * 所以下面的initHandlerMapping()可以拿到属性
         */
        this.webApplicationContext = new AnnotationConfigWebApplicationContext(this.sContextConfigLocation, this.parentApplicationContext);

        Refresh();
    }

    protected void Refresh() {

        initHandlerMapping(this.webApplicationContext);
        initHandlerAdapter(this.webApplicationContext);
    }

    protected void initHandlerMapping(WebApplicationContext webApplicationContext) {
        this.handlerMapping = new RequestMappingHandlerMapping(webApplicationContext);
    }

    protected void initHandlerAdapter(WebApplicationContext webApplicationContext) {
        this.handlerAdapter = new RequestMappingHandlerAdapter(webApplicationContext);
    }


    protected void service(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE,this.webApplicationContext);
        try{
            doDispatch(request,response);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {

        }
    }

    /**
     *  TODO 测试需要解决Tomcat引入项目的问题
     *  这一样完结之前需要做一次测试哦
     *  重新走一遍流程
     *
     * @param request
     * @param response
     * @throws Exception
     */
    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpServletRequest processedRequest = request;
        HandlerMethod handlerMethod = null;
        try {
            handlerMethod = this.handlerMapping.getHandler(processedRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(handlerMethod == null){
            return;
        }
        HandlerAdapter handlerAdapter = this.handlerAdapter;
        handlerAdapter.handle(request,response,handlerMethod);

    }


}
