package com.demo.code.MyObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<MyObject> myList = Arrays.asList(
                new MyObject("A1", "B1","A3B1"),
                new MyObject("A1", "B1","A4B1"),
                new MyObject("A1", "B3","A6B1"),
                new MyObject("A2", "B2","A2B1"),
                new MyObject("A2", "B2","A3B1"),
                new MyObject("A2", "B3","A6B1")
        );

        Map<String, Map<String, List<String>>> collect = myList.stream()
                .collect(Collectors.groupingBy(MyObject::getPropertyA,
                        Collectors.groupingBy(MyObject::getPropertyB,Collectors.mapping(MyObject::getDeviceCode,Collectors.toList()))));
        // 打印结果
        ArrayList<MyObject> myObjects = new ArrayList<>();
        collect.forEach((key,value)-> value.forEach((keyInner, valueInner)->{
            MyObject pollDetailSubVo = new MyObject();
            pollDetailSubVo.setPropertyA(key);
            pollDetailSubVo.setPropertyB(keyInner);
            pollDetailSubVo.setList(valueInner);
            myObjects.add(pollDetailSubVo);
        }));

        System.out.println(myObjects);

    }
}
