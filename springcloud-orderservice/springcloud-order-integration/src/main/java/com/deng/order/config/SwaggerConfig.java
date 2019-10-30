package com.deng.order.config;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

	@Bean
	public Docket createRestApi() {
//		ParameterBuilder tokenPar = new ParameterBuilder();
//    	List<Parameter> pars = new ArrayList<Parameter>();
//    	pars.add(tokenPar.build());

		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.deng.order.controller"))
				.paths(PathSelectors.any())
				.build();
//				.globalOperationParameters(pars);
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("订单服务api文档")
				.description("简单优雅的restfun风格")
				.termsOfServiceUrl("")
				.version("1.0")
				.build();
	}
	
	@Override

	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	// TODO Auto-generated method stub
		super.configureMessageConverters(converters);
		converters.add(responseBodyConverter());
	}

	@Bean

	public HttpMessageConverter<?> responseBodyConverter() {
	    StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
	    return converter;

	}
	
	@Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
