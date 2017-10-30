
package com.trs.gallery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Title: DOPApplication.java 
 * @Description: 通过main方法启动工程
 * @author chenli
 * @date 2016年12月13日 上午11:18:36
 * @version V1.0.0   
 */
@SpringBootApplication
@MapperScan("com.trs.gallery.mapper")
@EnableScheduling
public class DOPApplication {
	
	public static void main(String[] args) throws Exception {
		// 加载配置文件
		SpringApplication application = new SpringApplication(DOPConfiguration.class, DOPApplication.class);
		// 加上全名称的spring bean名称构造器
		application.setBeanNameGenerator(new FullBeanNameGenerator());
		application.run(args);
	}
	
}
