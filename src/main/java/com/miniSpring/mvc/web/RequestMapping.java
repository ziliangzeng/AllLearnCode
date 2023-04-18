package com.miniSpring.mvc.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO 这里我的问题就是，
 * 什么方法可以扫描当前目录下所有的类，然后把这些类的信息都读取出来？
 * 应该就是遍历所有的目录下的数据，然后就是遍历方法里面的注解
 * 这里其实有很多分支的呢？一个类有很多注解，一个方法有很多注解
 * 然后都是不断读处理，再将其添加到不同集合中哈哈哈
 * 应该是这个思路吧？哈哈哈
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    String value() default "";

}
