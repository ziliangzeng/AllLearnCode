package com.learn.code.minioSpring.com.minis;

import java.util.Iterator;

/**
 * 把外部的配置信息都当成Resource（资源），来进行抽象
 */
public interface Resource extends Iterator<Object> {
}
