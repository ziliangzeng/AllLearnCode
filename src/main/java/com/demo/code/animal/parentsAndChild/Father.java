package com.demo.code.animal.parentsAndChild;

import lombok.Data;

@Data
public class Father {

    private String id;
    private String name;
    private String age;

    public Father(String id){
        this.id = id;
    }

    public Father(String id,String name){
        this.id = id;
        this.name = name;
    }

    public Father(String id,String name,String age){
        this.id = id;
        this.name = name;
        this.age = age;
    }


    public Father() {

    }
}
