package com.miniSpring.mvc.web;

public interface PropertyEditor {

    void setAsText(String text);
    void setValue(Object value);
    Object getAsText();
    Object getValue();
}
