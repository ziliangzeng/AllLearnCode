package com.learn.code.minioSpring.com.minis;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionReader {

    AutowireCapableBeanFactory beanFactory;

    /**
     * 后面看源码可以先看构造器，然后reader->load 这类型的方法啦，命名都是有规律的。
     * 看源码的可以找这些规律，然后自己写代码的时候，也可以这样命名，这样的话，代码的可读性就会更高
     *
     * @param beanFactory
     */
    public XmlBeanDefinitionReader(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 这里很明显的，需要有一个beanFactory的实现类，来存储正式的bean实例对象
     *
     * @param resource
     */
    public void loadBeanDefinitions(Resource resource) {
        //这里就是承上启下了，迭代器的设计模式，就是这样的
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String id = element.attributeValue("id");
            String aClass = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(id, aClass);

            List<Element> propertyElements = element.elements("property");
            PropertyValues pvs = new PropertyValues();
            List<String> refs = new ArrayList<>();
            for (Element e : propertyElements) {
                String name = e.attributeValue("name");
                String value = e.attributeValue("value");
                String type = e.attributeValue("type");
                String ref = e.attributeValue("ref");
                String pv = "";
                boolean isRef = false;
                //这里没有问题,就是判断ref是否存在值
                //那么我是否可以理解就是,在<property>标签中,value和ref是不能共存的呢?
                //因为这里是一个if else的判断,如果value存在值,那么就不会去判断ref是否存在值
                //那么怎么控制的呢? 我感觉应该是 if if 的关系才对
                if (value != null && !value.equals("")) {
                    isRef = false;
                    pv = value;
                } else if (ref != null && !ref.equals("")) {
                    isRef = true;
                    pv = ref;
                    refs.add(ref);
                }
                pvs.addPropertyValue(new PropertyValue(name, pv, type, isRef));
            }
            beanDefinition.setPropertyValues(pvs);

            //循环依赖递归
            //那么创建对象createBean的时候就需要不断的循环下去去创建对象，直到所有的对象都创建完毕。
            //当list集合泛型为String的时候,那么不需要指定数组的大小,可以直接new String[0] TODO 为什么？
            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);

            List<Element> constructorElements = element.elements("constructor-arg");
            ArgumentValues argumentValues = new ArgumentValues();
            for (Element e : constructorElements) {
                String name = e.attributeValue("name");
                String value = e.attributeValue("value");
                String type = e.attributeValue("type");
                argumentValues.addArgumentValue(new ArgumentValue(name, value, type));
            }
            beanDefinition.setConstructorArgumentValues(argumentValues);

            this.beanFactory.registerBeanDefinition(beanDefinition.getId(), beanDefinition);
        }
    }


}
