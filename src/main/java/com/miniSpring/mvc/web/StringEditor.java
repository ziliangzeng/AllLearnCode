package com.miniSpring.mvc.web;

/**
 * String转换器
 */
public class StringEditor implements PropertyEditor {

    private Class<String> strClass;
    private String strFormat;
    private boolean allowEmpty;
    private Object value;

    public StringEditor(Class<String> strClass, boolean allowEmpty) {
        this(strClass, null, allowEmpty);
    }

    public StringEditor(Class<String> strClass, String strFormat, boolean allowEmpty) {
        this.strClass = strClass;
        this.strFormat = strFormat;
        this.allowEmpty = allowEmpty;
    }


    @Override
    public void setAsText(String text) {
        setValue(text);
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Object getAsText() {
        return value.toString();
    }

    @Override
    public Object getValue() {
        return this.value;
    }
}
