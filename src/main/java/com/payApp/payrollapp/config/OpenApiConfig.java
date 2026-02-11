package com.payApp.payrollapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI payrollOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("PayRoll Service Api")
                        .description("API documentation for Payroll Processing System")
                        .version("v1.0")
                );
    }
}
