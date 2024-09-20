package com.datatab;

import com.datatab.properties.SmsProducerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {SmsProducerProperties.class})
public class SmsProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsProducerApplication.class, args);
    }


}
