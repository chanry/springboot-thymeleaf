package com.trs.gallery;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;


/**
 * @ClassName FullBeanNameGenerator
 * @Description Generator - 全路径bean名称构造器
 * @author chenli
 * @Date 2017年3月2日 上午10:14:28
 * @version 1.0.0
 */
public class FullBeanNameGenerator extends AnnotationBeanNameGenerator {

	@Override
	protected String buildDefaultBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		return definition.getBeanClassName();
	}

}
