package com.demo.code.exception;

public class MyClass {

    public void doSomethingAsync(Callback callback) {
        // 模拟异步操作
        new Thread(() -> {
            try {

                // 执行某个操作，这里以字符串"result"为例
                String result = "result";

                // 模拟操作中可能出现的异常
                try {

                    int a = 1 / 0; // 将此行取消注释以模拟异常

                } catch (Exception e) {
                    throw new Exception(e);
                }

                // 执行成功，调用onSuccess回调方法，并将结果传递给回调函数

                callback.onSuccess(result);

            } catch (Exception e) {
                // 捕获异常，调用onError回调方法，并将异常传递给回调函数
                callback.onError(e);
            }
        }).start();
    }
}

