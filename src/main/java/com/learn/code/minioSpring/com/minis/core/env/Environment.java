package com.learn.code.minioSpring.com.minis.core.env;

/**
 * 用于获取属性
 *
 * @Auther: qsx00
 * @Date: 2023/4/3 22:42
 * @Description:
 */
public interface Environment extends PropertyResolver {
    String[] getActiveProfiles();
    String[] getDefaultProfiles();
    boolean acceptsProfiles(String... profiles);
}
