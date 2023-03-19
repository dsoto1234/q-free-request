package com.conduent.ibts.alpr.process.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This class is created for create swagger configuration.
 * 
 * 
 * @version 1.0
 * @since 27-04-2021
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);

	public SwaggerConfig() {
		logger.info("Inside SwaggerConfig constructor()");
	}
	
	@Bean
	public Docket api() {
		logger.info("Inside SwaggerConfig.api()");
		
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.conduent.ibts."))
				.paths(PathSelectors.any()).build();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		logger.info("Inside SwaggerConfig.addResourceHandlers()");

		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
