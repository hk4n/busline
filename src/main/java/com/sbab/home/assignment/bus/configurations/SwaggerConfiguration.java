package com.sbab.home.assignment.bus.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sbab.home.assignment.bus"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInformation());
    }

    private ApiInfo apiInformation() {
        return new ApiInfo(
                "Bus line information and statistics API",
                "REST API to get bus line and stop information",
                "1.0",
                "Terms of service",
                ApiInfo.DEFAULT_CONTACT,
                "",
                "",
                new ArrayList<>());
    }
}
