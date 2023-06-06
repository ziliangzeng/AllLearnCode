package com.miniSpring.mvc.web;

import com.miniSpring.ioc.BeansException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class RequestMappingHandlerMapping implements HandlerMapping{

    WebApplicationContext wac;
    private final MappingRegistry mappingRegistry = new MappingRegistry();

    /**
     * 在DispatcherServlet中调用吧
     * @param wac
     */
    public RequestMappingHandlerMapping(WebApplicationContext wac) {
        this.wac = wac;
        initMapping();
    }

    //建立URL与调用方法和实例的映射关系，存储在mappingRegistry中
    protected void initMapping() {
        Class<?> clz = null;
        Object obj = null;
        String[] controllerNames = this.wac.getBeanDefinitionNames();

        for (String controllerName : controllerNames) {
            try {
                /**
                 * 并且这里的wac是传的AnnotationConfigWebApplicationContext，因为在DispatcherServlet中？
                 */
                clz = Class.forName(controllerName);

                obj = this.wac.getBean(controllerName);

            } catch (ClassNotFoundException | BeansException e) {
                throw new RuntimeException(e);
            }
            Method[] methods = clz.getDeclaredMethods();
            if(methods != null){
                for (Method method : methods) {
                    boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                    if(isRequestMapping){
                        String urlMappings = method.getAnnotation(RequestMapping.class).value();

                        this.mappingRegistry.getUrlMappingNames().add(urlMappings);
                        this.mappingRegistry.getMappingObjs().put(urlMappings,obj);
                        this.mappingRegistry.getMappingMethods().put(urlMappings,method);
                    }
                }
            }
        }

    }


    @Override
    public HandlerMethod getHandler(HttpServletRequest request) throws Exception {

        String sPath = request.getServletPath();
        if(!this.mappingRegistry.getUrlMappingNames().contains(sPath)){
            return null;
        }
        Object o = this.mappingRegistry.getMappingObjs().get(sPath);
        Method method = this.mappingRegistry.getMappingMethods().get(sPath);
        HandlerMethod handlerMethod = new HandlerMethod(method, o);

        return handlerMethod;
    }
}
