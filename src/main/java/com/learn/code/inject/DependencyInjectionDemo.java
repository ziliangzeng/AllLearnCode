package com.learn.code.inject;

import com.learn.code.entity.UserRepository;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Auther: qsx00
 * @Date: 2023/3/8 22:06
 * @Description:
 */
public class DependencyInjectionDemo {

    public static void main(String[] args) {

        /**
         * 1,applicationContext是beanFactory的子接口
         * 2,下面两个beanFactory不相等, 通过AbstractApplicationContext#refresh()哪里进去可以知道,注入的是org.springframework.context.support.AbstractRefreshableApplicationContext#beanFactory
         * 跟ClassPathXmlApplicationContext没有上下级关系,最多表兄弟
         * TODO refresh()是在什么时候被调用的呢?不知道...
         * 需要知道项目启动的流程的调用全过程,后面看调用栈.
         *
         */
        /**
         * ApplicationContext是BeanFactory的子接口，说明ApplicationContext is BeanFactory。并且ApplicationContext 是BeanFactory的包装类，也就是内部组合了BeanFactory的实现-DefaultListableBeanFactory。为什么包装了DefaultListableBeanFactory，因为它需要简化且丰富功能来为企业开发提供更高的便捷性，也就是说ApplicationContext 是DefaultListableBeanFactory的超集。
         * 至于为什么UserRepository注入的BeanFactory  不等于ClassPathXmlApplicationContext得到的BeanFactory ，是因为AbstractApplicationContext#prepareBeanFactory中 指明了 beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory); 也就是说当byType是BeanFactory.class的时候，获得是的ApplicationContext中的DefaultListableBeanFactory对象。
         * 那真正的IOC的底层实现就是BeanFactory的实现类，因为ApplicationContext是委托DefaultListableBeanFactory来操作getBean等方法的。
         */
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:META-INF/dependency-injection-context.xml");
        UserRepository userRepository = beanFactory.getBean("userRepository", UserRepository.class);

        System.out.println(beanFactory);
        System.out.println("-------------");
        System.out.println(userRepository.getBeanFactory());

        System.out.println(userRepository.getBeanFactory() == beanFactory);
    }
}
