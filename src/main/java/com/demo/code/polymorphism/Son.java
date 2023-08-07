package com.demo.code.polymorphism;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Son extends Father{

    private String sonName;


    public String getSonName() {
        return sonName;
    }


    public void getSonNamess() {
        System.out.println("sonName");
    }

}
