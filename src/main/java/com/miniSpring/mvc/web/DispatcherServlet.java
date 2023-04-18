package com.miniSpring.mvc.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 根本性需要Servlet支持
 * 然后就是url路径和方法的映射需要存储
 * 然后再在业务层使用反射调用方法的形式执行方法
 *
 */
public class DispatcherServlet extends HttpServlet {

    private Map<String, MappingValue> mappingValues;
    private Map<String, Class<?>> mappingClz = new HashMap<>();
    private Map<String, Object> mappingObjs = new HashMap<>();


    private String sContextConfigLocation;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        /**
         * 这里对应上了web.xml中的配置
         *         <init-param>
         *             <param-name>contextConfigLocation</param-name>
         *             <!--初始化配置文件地址，所有的配置参数都由这里引入-->
         *             <param-value>/WEB-INF/minisMVC-servlet.xml</param-value>
         *         </init-param>
         */
        sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;

        try {
            //拿到xml配置地址
            xmlPath = this.getServletContext().getResource(sContextConfigLocation);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        //资源加载和解析
        Resource rs = new ClassPathXmlResource(xmlPath);
        XmlConfigReader reader = new XmlConfigReader();
        mappingValues = reader.loadConfig(rs);
        Refresh();
    }

    protected void Refresh() {
        for (Map.Entry<String, MappingValue> entry : mappingValues.entrySet()) {
            String id = entry.getKey();
            String className = entry.getValue().getClz();
            Object obj = null;
            Class<?> clz = null;
            try {
              clz = Class.forName(className);
              obj = clz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            mappingClz.put(id, clz);
            mappingObjs.put(id, obj);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String servletPath = request.getServletPath();
        if(this.mappingValues.get(servletPath) == null){
            return;
        }
        Class<?> clz = this.mappingClz.get(servletPath);
        Object obj = this.mappingObjs.get(servletPath);

        String methodName = this.mappingValues.get(servletPath).getMethod();
        Object invoke = null;
        try {
            /**
             * TODO paramType[] 和 paramValue[] 这两个都还没有，所以需要支持下面支持。
             */
            Method method = clz.getMethod(methodName);
            invoke = method.invoke(obj);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        response.getWriter().append(invoke.toString());
    }




}
