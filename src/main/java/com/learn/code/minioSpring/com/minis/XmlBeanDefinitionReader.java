package com.learn.code.minioSpring.com.minis;

import org.dom4j.Element;

import java.util.List;

public class XmlBeanDefinitionReader {

    BeanFactory beanFactory;

    /**
     * 后面看源码可以先看构造器，然后reader->load 这类型的方法啦，命名都是有规律的。
     * 看源码的可以找这些规律，然后自己写代码的时候，也可以这样命名，这样的话，代码的可读性就会更高
     *
     * @param beanFactory
     */
    public XmlBeanDefinitionReader(BeanFactory beanFactory){
        this.beanFactory = beanFactory;
    }

    /**
     * 这里很明显的，需要有一个beanFactory的实现类，来存储正式的bean实例对象
     *
     * @param resource
     */
    public void loadBeanDefinitions(Resource resource){
        //这里就是承上启下了，迭代器的设计模式，就是这样的
        while(resource.hasNext()){
            Element element = (Element)resource.next();
            String id = element.attributeValue("id");
            String aClass = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(id, aClass);

            List<Element> propertyElements = element.elements("property");
            PropertyValues pvs = new PropertyValues();
            for (Element e : propertyElements) {
                String name = e.attributeValue("name");
                String value = e.attributeValue("value");
                String type = e.attributeValue("type");
                pvs.addPropertyValue(new PropertyValue(name, value, type));
            }
            beanDefinition.setPropertyValues(pvs);


            List<Element> constructorElements = element.elements("constructor-arg");
            ArgumentValues argumentValues = new ArgumentValues();
            for (Element e : constructorElements) {
                String name = e.attributeValue("name");
                String value = e.attributeValue("value");
                String type = e.attributeValue("type");
                argumentValues.addArgumentValue(new ArgumentValue(name, value, type));
            }
            beanDefinition.setConstructorArgumentValues(argumentValues);


            this.beanFactory.registerBeanDefinition(beanDefinition);
        }
    }


}
