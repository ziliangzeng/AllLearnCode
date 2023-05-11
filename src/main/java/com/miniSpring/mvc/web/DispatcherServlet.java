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

/**
 *
 * 根本性需要Servlet支持
 * 然后就是url路径和方法的映射需要存储
 * 然后再在业务层使用反射调用方法的形式执行方法
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
                clz = Class.forName(controllerName);
                controllerClasses.put(controllerName,clz);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
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
            invoke = method.invoke(o);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }


        response.getWriter().append(invoke.toString());
    }




}
