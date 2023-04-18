package com.miniSpring.mvc.web;

import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

public class XmlConfigReader {
    public XmlConfigReader() {
    }


    public Map<String, MappingValue> loadConfig(Resource res) {
        Map<String, MappingValue> mappings = new HashMap<>();
        while (res.hasNext()) {
            Element element = (Element) res.next();
            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            String beanMethod = element.attributeValue("value");
            mappings.put(beanId, new MappingValue(beanId, beanClassName, beanMethod));
        }
        return mappings;
    }

}
