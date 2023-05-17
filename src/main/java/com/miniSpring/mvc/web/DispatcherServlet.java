package com.miniSpring.mvc.web;


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
 *
 * 根本性需要Servlet支持
 * 然后就是url路径和方法的映射需要存储
 * 然后再在业务层使用反射调用方法的形式执行方法
 *
 *
 *
 *
 * SpringMVC的容器直接管理跟DispatcherServlet相关的Bean，也就是Controller，ViewResolver等，并且SpringMVC容器是在DispacherServlet的init方法里创建的。
 * 而Spring容器管理其他的Bean比如Service和DAO。
 * 并且SpringMVC容器是Spring容器的子容器，
 * 所谓的父子关系意味着什么呢，就是你通过子容器去拿某个Bean时，子容器先在自己管理的Bean中去找这个Bean，如果找不到再到父容器中找。但是父容器不能到子容器中去找某个Bean。
 *
 *
 *
 */
public class DispatcherServlet extends HttpServlet {


    private List<String> packageNames = new ArrayList<>();
    private List<String> controllerNames = new ArrayList<>();
    private Map<String,Class<?>> controllerClasses = new HashMap<>();
    private Map<String,Object> controllerObjs = new HashMap<>();

    private List<String> urlMappingNames = new ArrayList<>();
    private Map<String, Object> mappingObjs = new HashMap<>();
    private Map<String,Method> mappingMethods = new HashMap<>();



    private Map<String, MappingValue> mappingValues;
    private Map<String, Class<?>> mappingClz = new HashMap<>();


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

        packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);


        Refresh();
    }

    protected void Refresh() {
        initController();
        initMapping();

    }

    private void initController() {

        this.controllerNames = scanPackages(this.packageNames);
        for (String controllerName : this.controllerNames) {
            Object obj = null;
            Class<?> clz = null;
            try {
                /**
                 * TODO 这里是否会出现就是不是ioc容器管理的类，那么注入这些能获取到吗
                 *
                 */
                clz = Class.forName(controllerName);
                controllerClasses.put(controllerName,clz);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                /**
                 * 没错，TODO  需要处理这里,需要拿到ioc容器里面的对象，因为里面的构造器和@Autowired都是已经注入完毕的
                 *
                 */
                obj = clz.newInstance();
                controllerObjs.put(controllerName,obj);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }


    }

    private void initMapping() {

        for (String controllerName : this.controllerNames) {

            Class<?> clazz = this.controllerClasses.get(controllerName);
            Object o = this.controllerObjs.get(controllerName);
            Method[] methods = clazz.getDeclaredMethods();
            if(methods !=null){
                for (Method method : methods) {

                    /**
                     * 这里是否是建ioc容器里面的service给拿出来的?
                     * 这样子就是可以实现注入的
                     * 这样子自动生成是否有问题
                     *
                     * 不对哦，之前学习的ioc容器拿到的值都是例如属性或者构造器里面的参数获取到数据的，跟方法好像还没有联动起来
                     *
                     * \
                     * TODO 如何与ioc容器进行联动呢
                     * 看一下源码可能会好一点。
                     *
                     */
                    boolean annotationPresent = method.isAnnotationPresent(RequestMapping.class);
                    if (annotationPresent){
                        String name = method.getName();
                        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                        String value = annotation.value();
                        this.urlMappingNames.add(value);
                        this.mappingObjs.put(value,o);
                        this.mappingMethods.put(value,method);
                    }
                }
            }
        }

    }


    private List<String> scanPackages(List<String> packages){

        List<String> tempControllerNames = new ArrayList<>();

        for (String aPackage : packages) {

            tempControllerNames.addAll(scanPackage(aPackage));
        }

        return tempControllerNames;
    }

    /**
     * 理解
     *
     * 为什么要进行这些格式的切换,因为在minisMVC-servlet.xml 定义的是扫描的目录路径,
     * 他的格式是//// 的
     * 而我们的类文件是 com.miniSpring.mvc.web.DispatcherServlet .....的
     * 所以要进行切换
     * 但是很明显,这里还没有区分哪个 Interface,emum @interface这些吧
     * 统一Class了
     *
     * TODO 这里很值得学,目录扫描,进而拿到class 里面的方法,都是一环接一环的
     *
     * @param aPackage
     * @return
     */
    private List<String> scanPackage(String aPackage) {
        ArrayList<String> tempControllerNames = new ArrayList<>();
        URI uri = null;
        try {
            uri = this.getClass().getResource("/" + aPackage.replaceAll("\\.","/")).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File dir = new File(uri);
        for (File file : dir.listFiles()) {
            if(file.isDirectory()){
                  scanPackage(aPackage + "." + file.getName());
            }else{
                String controllerName = aPackage + "." + file.getName().replace(".class","");
                tempControllerNames.add(controllerName);
            }
        }
        return tempControllerNames;
    }

    /**
     * 什么都涉及反射reflect
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param response  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String servletPath = request.getServletPath();
        if(!this.urlMappingNames.contains(servletPath)){
            return;
        }

        Method method = this.mappingMethods.get(servletPath);
        Object o = this.mappingObjs.get(servletPath);

        Object invoke = null;
        try {

            /**
             * 准确的来讲，应该是这里的来去获取ioc容器的对象的吧?
             * //TODO 有点忘记怎么@Autowired注入的了
             */
            invoke = method.invoke(o);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }


        response.getWriter().append(invoke.toString());
    }




}
