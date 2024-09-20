package com.datatab.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("com.datatab")
public record SmsProducerProperties(
        String sendSmsQueueName) {
}
