/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author User
 */
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext appContext;

    // Private constructor prevents instantiation from other classes
    private ApplicationContextUtils() {
        System.out.println("ApplicationContextUtils");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        System.out.println("setApplicationContext");
//        String profile = System.getenv("ambiente");
//        applicationContext.getEnvironment().
        appContext = applicationContext;
        //appContext.getEnvironment().setActiveProfiles( "myProfile" );
    }

    public static Object getBean(String beanName) {
        System.out.println("ApplicationContextUtils - getBean");
        return appContext.getBean(beanName);
    }
}
