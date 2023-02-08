package com.example.creditCardPayment.util.propertyfile.beanfacto;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import com.example.creditCardPayment.util.context.handler.ContextRefreshedEventHandler;



public class BeanCreator extends ContextRefreshedEventHandler {

	private static ApplicationContext context;

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T getBeanInstance(Class<T> clazz) {
		return context.getBean(clazz);
	}

	public void onApplicationEvent(ContextRefreshedEvent event) {
		context = event.getApplicationContext();
	}
}
