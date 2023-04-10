package com.miniSpring.ioc;

/**
 * 很明显，这是要做映射的吧？
 * 对应的<bean></bean> 标签中的<constructor-arg></constructor-arg>标签
 * <property></property>标签 中的 type name value
 */
public class ArgumentValue {
    private Object value;
    private String type;
    private String name;

    public ArgumentValue(Object value,String type){
        this.value = value;
        this.type = type;
    }

    public ArgumentValue(String name,Object value, String type) {
        this.value = value;
        this.type = type;
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
