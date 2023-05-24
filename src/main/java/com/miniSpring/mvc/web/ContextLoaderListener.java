package com.miniSpring.mvc.web;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 要记住一个顺序
 * servletContext  context-param
 * listener
 * filter
 * servlet
 */

/**
 * 这里的时序，也就是功能的实现前提 挺重要的！
 * 就跟bean 的生命周期一样，实例化（构造器注入） ，属性注入（依赖注入），初始化，销毁
 *
 *
 * 当 Servlet 服务器如 Tomcat 启动的时候，要遵守下面的时序。
 * 1.在启动 Web 项目时，Tomcat 会读取 web.xml 中的 context-param 节点，获取这个 Web 应用的全局参数。
 * 2.Tomcat 创建一个 ServletContext 实例，是全局有效的。
 * 3.将 context-param 的参数转换为键值对，存储在 ServletContext 里。
 * 4.创建 listener 中定义的监听类的实例，按照规定 Listener 要继承自 ServletContextListener。
 *   监听器初始化方法是 contextInitialized(ServletContextEvent event)。
 *   初始化方法中可以通过 event.getServletContext().getInitParameter(“name”) 方法获得上下文环境中的键值对。
 * 5.当 Tomcat 完成启动，也就是 contextInitialized 方法完成后，
 * 再对 Filter 过滤器进行初始化。
 * 6.servlet 初始化：有一个参数 load-on-startup，它为正数的值越小优先级越高，会自动启动，如果为负数或未指定这个参数，会在 servlet 被调用时再进行初始化。init-param 是一个 servlet 整个范围之内有效的参数，在 servlet 类的 init() 方法中通过 this.getInitParameter(″param1″) 方法获得
 *
 *
 */

public class ContextLoaderListener implements ServletContextListener {

    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";

    private WebApplicationContext webApplicationContext;

    public ContextLoaderListener(){}

    public ContextLoaderListener(WebApplicationContext webApplicationContext){
        this.webApplicationContext = webApplicationContext;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce){
        initWebApplicationContext(sce.getServletContext());
    }

    public void initWebApplicationContext(ServletContext servletContext){
        //这里是拿到web.xml里面的配置文件参数属性吧
        //获取ioc容器的路径
        String configLocation = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
        //那我是不是理解为springboot理解为内嵌的tomcat启动的时候，会去读取配置文件，然后去初始化容器，然后去初始化servlet
        //
        /**
         * TODO 这里其实是不是可以直接从外部引用的呢，然后直接注入，这样子就可以实现复用了？
         * 但是这样子就要保证外部引用的applicationContext初始化,
         * 但是现在java程序都是(?)在Servlet容器中运行的，所以在这里初始化也没有问题吧
         *
         */
        WebApplicationContext wac = new XmlWebApplicationContext(configLocation);
        wac.setServletContext(servletContext);
        this.webApplicationContext = wac;
        //这里是给全局参数设值?
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wac);
    }


}
