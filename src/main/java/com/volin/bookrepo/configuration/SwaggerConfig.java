package com.volin.bookrepo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig - конфигуратор Swagger.
 * 1. Является Spring конфигурацией.
 * 2. Включает возможность использования Swagger аннотаций 
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * Api.
     *
     * @return the docket
     */
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("com.volin.bookrepo.controller"))              
          .paths(PathSelectors.any())                          
          .build();                                           
    }

}
