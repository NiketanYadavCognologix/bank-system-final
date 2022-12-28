package com.cognologix.bankingApplication.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket postApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("com.cognologix.bankingApplication")                             //package name where there is main class
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .build();
    }
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Banking Application by Cognologix")             	//custom (programmer define) name for Page Title
                .description("Banking application")		                                    //custom (programmer define) description
                .termsOfServiceUrl("http://www.cognologix.training.bankApplication.com")	//custom (programmer define) url for terms
                .license("Licence @ Niketan")									            //custom (programmer define) license name
                .licenseUrl("http://www.niketanApp.com/licence")				        	//custom (programmer   define) license url
                .version("1.0.0")												        	//custom version
                .build();

        //here custom values are the programmer given values, u

    }
}
