package com.datatab.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("sms-consumer")
                        .description("sms-consumer")
                        .version("v1.0.0")
                        .contact(new Contact().name("Data Tab").url("https://github.com/muraguri2005").email("muraguri2005@gmail.com"))
                        .termsOfService("")
                );
    }
}
