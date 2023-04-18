package com.miniSpring.mvc.web;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 相当于把Resource和Reader合并了
 */
public class XmlScanComponentHelper {


    public static List<String> getNodeValue(URL xmlPath){

        ArrayList<String> packages = new ArrayList<>();
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(xmlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * TODO 这种写法是否有问题的呢还是要判断root节点是什么的吧？例如判断他的
         * String rootName = root.getName();
         * rootName.equals("components")
         */
        Element root = document.getRootElement();

        //返回root的子元素的迭代器
        Iterator it = root.elementIterator();

        while(it.hasNext()){
            Element element = (Element) it.next();
            String name = element.getName();
            if (name.equals("component-scan")){
                String basePackage = element.attributeValue("base-package");
                packages.add(basePackage);
            }
        }
        return packages;
    }



}
