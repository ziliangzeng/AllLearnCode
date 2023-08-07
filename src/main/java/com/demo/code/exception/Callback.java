package com.demo.code.exception;

public interface Callback {
    void onSuccess(String result);
    void onError(Exception e);
}
