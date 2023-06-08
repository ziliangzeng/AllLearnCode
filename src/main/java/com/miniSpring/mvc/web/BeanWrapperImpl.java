package com.miniSpring.mvc.web;

import com.miniSpring.ioc.PropertyValue;
import com.miniSpring.ioc.PropertyValues;
import org.springframework.beans.PropertyEditorRegistrySupport;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * bean包装类?
 */
public class BeanWrapperImpl extends PropertyEditorRegistrySupport {

    Object wrappedObject; //目标对象
    Class<?> clz;
    PropertyValues pvs;

    public BeanWrapperImpl(Object wrappedObject) {
        registerDefaultEditors();
        this.wrappedObject = wrappedObject;
        //反射
        this.clz = wrappedObject.getClass();
    }

    public void setBeanInstance(Object object) {
        this.wrappedObject = object;
    }

    public Object getBeanInstance() {
        return this.wrappedObject;
    }

    //绑定参数值
    public void setPropertyValues(PropertyValues pvs) {
        this.pvs = pvs;
        for (PropertyValue pv : this.pvs.getPropertyValues()) {
            setPropertyValue(pv);
        }
    }

    //绑定目标某个具体参数
    public void setPropertyValue(PropertyValue pv) {
        //拿到参数处理器
        BeanPropertyHandler propertyHandler = new BeanPropertyHandler(pv.getName());
        PropertyEditor pe = (PropertyEditor) this.getDefaultEditor(propertyHandler.getPropertyClz());
        pe.setAsText((String) pv.getValue());
        propertyHandler.setValue(pe.getValue());

    }


    //内部类，用于处理参数，通过getter()和setter()操作属性
    class BeanPropertyHandler {
        Method writeMehtod = null;
        Method readMethod = null;
        Class<?> propertyClz = null;

        public Class<?> getPropertyClz() {
            return propertyClz;
        }

        public BeanPropertyHandler(String propertyName){
            try{
                Field field = clz.getDeclaredField(propertyName);
                //属性的类型
                propertyClz = field.getType();
                //获取设置属性的帆帆，按照约定为setXxxx();
                this.writeMehtod = clz.getDeclaredMethod("set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1), propertyClz);
                this.readMethod = clz.getDeclaredMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1), propertyClz);
            } catch (NoSuchFieldException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }


        public Object getValue(){
            Object result = null;
            writeMehtod.setAccessible(true);
            try{
                result = readMethod.invoke(wrappedObject);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return result;
        }


        public void setValue(Object value){
            writeMehtod.setAccessible(true);
            try{
                writeMehtod.invoke(wrappedObject,value);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }



    }


}
