package com.tajdingurdal.app;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * You can get access for any spring bean with this class.
 * And we defined this class in main class as bean.
 */
public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }

    //You can get access for any spring bean with this method.
    public static Object getBean(String beanName) {
        return CONTEXT.getBean(beanName);
    }
}
