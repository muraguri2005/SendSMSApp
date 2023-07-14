package org.freelesson.sendsms.config;

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
                .info(new Info().title("SendSMS API")
                        .description("SendSMS API")
                        .version("v0.0.1")
                        .contact(new Contact().name("Free Lesssons Limited").url("https://github.com/muraguri2005").email("muraguri2005@gmail.com"))
                        .termsOfService("")
                );
    }
}
