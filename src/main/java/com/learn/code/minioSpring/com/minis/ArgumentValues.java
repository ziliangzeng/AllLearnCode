package com.learn.code.minioSpring.com.minis;

import org.apache.tomcat.util.bcel.classfile.ArrayElementValue;

import java.util.*;

/**
 * 属性集合类
 */
public class ArgumentValues {
    private final Map<Integer,ArgumentValue> indexedArgumentValues = new HashMap<>(0);
    private final List<ArgumentValue> genericArgumentValues = new LinkedList<>();
    public ArgumentValues() {
    }

    private void addArgumentValue(int index, ArgumentValue value) {
        this.indexedArgumentValues.put(index, value);
    }

    public boolean hasIndexedArgumentValue(int index) {
        return this.indexedArgumentValues.containsKey(index);
    }

    public ArgumentValue getIndexedArgumentValue(int index){
        return this.indexedArgumentValues.get(index);
    }

    /**
     * 舍弃掉name的原因是什么呢？
     *
     * @param value
     * @param type
     */
    public void addGenericArgumentValue(Object value,String type) {
        this.genericArgumentValues.add(new ArgumentValue(value,type));
    }


    private void addGenericArgumentValue(ArgumentValue newValue) {
        if(newValue.getName() != null){
            for(Iterator<ArgumentValue> it = this.genericArgumentValues.iterator();it.hasNext();){
                ArgumentValue value = it.next();
                if(newValue.getName().equals(value.getName())){
                    it.remove();
                }
            }
        }
        this.genericArgumentValues.add(newValue);
    }

    public ArgumentValue getGenericArgumentValue(String requiredName){
        for (ArgumentValue valueHolder : this.genericArgumentValues) {
            if(valueHolder.getValue() != null && (requiredName == null || !valueHolder.getName().equals(requiredName))){
                continue;
            }
            return valueHolder;
        }
        return null;
    }

    public int getArgumentCount() {
        return this.genericArgumentValues.size();
    }

    public boolean isEmpty() {
        return this.genericArgumentValues.isEmpty();
    }

}
