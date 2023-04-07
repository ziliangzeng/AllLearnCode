package com.learn.code.minioSpring.com.minis;

import java.util.ArrayList;
import java.util.List;

/**
 * 目前负责功能：添加后置处理器
 * 执行后置处理器的逻辑
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowiredCapableBeanFactory{

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


//    private final List<AutowiredAnnotationBeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public void addBeanPostProcessor(AutowiredAnnotationBeanPostProcessor beanPostProcessor) {
        beanPostProcessors.remove(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
    }

    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }

    public List<BeanPostProcessor> getBeanPostProcessors(){
        return this.beanPostProcessors;
    }

    /**
     * 这个应该是重写的哪个的啊？
     * 这里抽象类和接口重复了
     *
     * TODO 这里我使用接口的来实现，把抽象类的给注释掉了
     * @param existingBean
     * @param beanName
     * @return
     */
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) {

        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            //多态，应该是AbstractBeanFactory类型的吧
            beanPostProcessor.setBeanFactory(this);
            try {
                result = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
            if(result == null){
                return result;
            }
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            //多态，应该是AbstractBeanFactory类型的吧
            beanPostProcessor.setBeanFactory(this);
            try {
                result = beanPostProcessor.postProcessAfterInitialization(result, beanName);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
            if(result == null){
                return result;
            }
        }
        return result;
    }

}
