package com.learn.code.minioSpring.com.minis;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowiredCapableBeanFactory{

    private final List<AutowiredAnnotationBeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public void addBeanPostProcessor(AutowiredAnnotationBeanPostProcessor beanPostProcessor) {
        beanPostProcessors.remove(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
    }

    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }

    public List<AutowiredAnnotationBeanPostProcessor> getBeanPostProcessors(){
        return this.beanPostProcessors;
    }


    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) {

        Object result = existingBean;
        for (AutowiredAnnotationBeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
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
        for (AutowiredAnnotationBeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
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
