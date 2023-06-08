package com.miniSpring.ioc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PropertyValues {

    private final List<PropertyValue> propertyValueList;

    public PropertyValues() {
        propertyValueList = new ArrayList<>();
    }

    /**
     * TODO 如何映射呢
     * 可知的是name value
     * type的话 可以通过value 获取到？
     * @param map
     */
    public PropertyValues(Map<String,Object> map){
        propertyValueList = null;
    }


    public List<PropertyValue> getPropertyValueList() {
        return propertyValueList;
    }

    public Integer size(){
        return propertyValueList.size();
    }

    public void addPropertyValue(PropertyValue propertyValue){
        propertyValueList.add(propertyValue);
    }

    public void addPropertyValue(String name, Object value){
        propertyValueList.add(new PropertyValue(name, value));
    }

    public void removePropertyValue(PropertyValue propertyValue){
        propertyValueList.remove(propertyValue);
    }

    public void removePropertyValue(String name){
        propertyValueList.removeIf(propertyValue -> propertyValue.getName().equals(name));
    }

    public PropertyValue[] getPropertyValues(){
        return propertyValueList.toArray(new PropertyValue[size()]);
    }

    public PropertyValue getPropertyValue(String name){
        for (PropertyValue propertyValue : propertyValueList) {
            if(propertyValue.getName().equals(name)){
                return propertyValue;
            }
        }
        return null;
    }

    public Object get(String name){
        PropertyValue propertyValue = getPropertyValue(name);
        return propertyValue == null ? null : propertyValue.getValue();
    }

    public boolean contains(String name){
        return getPropertyValue(name) != null;
    }

    public boolean isEmpty(){
        return propertyValueList.isEmpty();
    }


}
