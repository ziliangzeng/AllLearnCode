package com.learn.code.minioSpring.com.minis;

import org.apache.tomcat.util.bcel.classfile.ArrayElementValue;

import java.util.*;

/**
 * 属性集合类
 */
public class ArgumentValues {
    private final List<ArgumentValue> argumentValueList = new ArrayList<>();

    public ArgumentValues() {
    }

    public void addArgumentValue(ArgumentValue value) {
        this.argumentValueList.add(value);
    }


    public ArgumentValue getIndexedArgumentValue(int index) {
        return this.argumentValueList.get(index);
    }


    public int getArgumentCount() {
        return this.argumentValueList.size();
    }

    public boolean isEmpty() {
        return this.argumentValueList.isEmpty();
    }

}
