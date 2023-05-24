package com.demo.code.animal.parentsAndChild;

import lombok.Data;

@Data
public class Child extends Father{

    private String child_id;
    private String child_name;
    private String child_age;

    public Child(){
        super();
    }

    public Child(String id){
//        super(id);
        this.child_id = id;
    }

    public String getId(){
        System.out.println("child_id:" + this.child_id);
        return this.child_id;
    }


}
