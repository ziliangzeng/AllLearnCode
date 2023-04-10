package com.miniSpring.ioc.env;

/**
 *
 * 容器增加一些环境因素，使一些容器整体所需要的属性有个地方存储访问
 *
 * @Auther: qsx00
 * @Date: 2023/4/3 22:40
 * @Description:
 */
public interface PropertyResolver {
    boolean containsProperty(String key);
    String getProperty(String key);
    String getProperty(String key, String defaultValue);
    <T> T getProperty(String key, Class<T> targetType);
    <T> T getProperty(String key, Class<T> targetType, T defaultValue);
    <T> Class<T> getPropertyAsClass(String key, Class<T> targetType);
    String getRequiredProperty(String key) throws IllegalStateException;
    <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException;
    String resolvePlaceholders(String text);
    String resolveRequiredPlaceholders(String text) throws IllegalArgumentException;
}
