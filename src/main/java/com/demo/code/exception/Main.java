package com.demo.code.exception;

public class Main {
    public static void main(String[] args) {
//        MyClass2 myClass2 = new MyClass2();
//
//        try{
//            myClass2.tets();
//        }catch (Exception e) {
//            System.out.println("catch");
//        }

        byte[] by = null;
        String s = new String(by);
        System.out.println(s);

    }
}

//public class Main {
//
//    public static void main(String[] args) {
//
//        MyClass myClass = new MyClass();
//
//        // 使用匿名内部类实现Callback接口
//        myClass.doSomethingAsync(new Callback() {
//            @Override
//            public void onSuccess(String result) {
//                System.out.println("操作成功，结果为：" + result);
//            }
//
//            @Override
//            public void onError(Exception e) {
//                // 在这里处理异常，可以根据实际需求进行处理
//                System.out.println("操作发生异常：" + e.getMessage());
//            }
//        });
//    }
//}



