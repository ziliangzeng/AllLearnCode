package com.demo.code.innerStatic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AllClass {

    private String name;
    private Inner inner;

    @Data
    @AllArgsConstructor
    public static class Inner {
        private String name;
    }

}
