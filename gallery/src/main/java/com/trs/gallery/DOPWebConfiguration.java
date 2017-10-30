package com.trs.gallery;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//只有在web环境才会启用该配置
@ConditionalOnWebApplication
//加入Controller注解扫描
@ComponentScan(includeFilters = @ComponentScan.Filter(classes = Controller.class))
public class DOPWebConfiguration extends WebMvcConfigurerAdapter {
	
	
}
