package com.datatab.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("com.datatab")
public record SmsConsumerProperties(
        String africasTalkingUsername,
        String africasTalkingPassword,
        String sendSmsQueueName,
        String sendSmsQueueGroupId) {
}
