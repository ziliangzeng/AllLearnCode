package com.miniSpring.mvc.web;

import com.miniSpring.ioc.PropertyValues;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class WebDataBinder {

    private Object target;
    private Class<?> clz;
    private String objectName;

    public WebDataBinder(Object target){
        this(target,"");
    }

    public WebDataBinder(Object target,String targetName){
        this.target = target;
        this.objectName = targetName;
        this.clz = this.target.getClass();
    }



    public void bind(HttpServletRequest request){


        /**
         * TODO 不是哦，应该这样子理解
         * 首先拿到了对应的方法了，
         * 根据HandlerMapping拿到了对应的方法
         * 方法里面有方法参数，然后一一映射吧
         * 这样子就会出现不能映射的情况哦
         *
         *
         * ?
         */


        //PropertyValue type name value 都有了
        PropertyValues mpvs = assignParameters(request);
        addBindValues(mpvs,request);
        doBind(mpvs);
    }

    private PropertyValues assignParameters(HttpServletRequest request) {

        Map<String, Object> map = WebUtils.getParametersStartingWith(request, "");
        return new PropertyValues(map);

    }


}
