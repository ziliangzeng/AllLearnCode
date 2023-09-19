package com.demo.code.MyObject;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class MyObject {
    private String propertyA;
    private String propertyB;
    private String deviceCode;
    private List<String> list;

    public MyObject(String propertyA, String propertyB,String deviceCode) {
        this.propertyA = propertyA;
        this.propertyB = propertyB;
        this.deviceCode = deviceCode;
    }

    public String getPropertyA() {
        return propertyA;
    }

    public String getPropertyB() {
        return propertyB;
    }
}

