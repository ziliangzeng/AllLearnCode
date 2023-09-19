package com.demo.code.exception;

public class MyClass2 {

    public void tets() {
        try {

//            try {
                //这里就是你定义的操作
                int i = 10 / 0;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        } catch (Exception e) {
            System.out.println("tets");
            throw e;
        }
    }

}
