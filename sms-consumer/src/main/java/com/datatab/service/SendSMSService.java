package com.datatab.service;

import com.africastalking.AfricasTalking;
import com.africastalking.sms.Recipient;
import com.datatab.domain.Sms;
import com.datatab.domain.enums.SmsStatus;
import com.datatab.properties.SmsConsumerProperties;
import com.datatab.repository.SmsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendSMSService {
    final SmsService smsService;
    final SmsRepository smsRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public SendSMSService(SmsService smsService, SmsRepository smsRepository, SmsConsumerProperties smsConsumerProperties, @Qualifier("objectMapper") ObjectMapper objectMapper) {
        AfricasTalking.initialize(smsConsumerProperties.africasTalkingUsername(), smsConsumerProperties.africasTalkingPassword());
        this.smsService = smsService;
        this.smsRepository = smsRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${com.datatab.send-sms-queue-name}", groupId = "${com.datatab.send-sms-queue-group-id}")
    public void sendSMSFromQueue(String smsString) {
        Sms sms = null;
        try {
            sms = objectMapper.readValue(smsString, Sms.class);
            com.africastalking.SmsService atSmsService = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
            log.info("sending sms via africastalking");
            List<Recipient> response = atSmsService.send(sms.message, "RMG", new String[]{sms.recipient}, true);
            sms.status = SmsStatus.SENT;
            log.info("sending sms response {}", response);
            if (!response.isEmpty()) {
                Recipient recipient = response.getFirst();
                sms.externalId = recipient.messageId;
                if (!recipient.status.equals("Success")) {
                    sms.status = SmsStatus.FAILED;
                    sms.statusComments = recipient.status;
                } else {
                    sms.status = SmsStatus.SENT;
                }
                sms.cost = Double.parseDouble(recipient.cost.split(" ")[1]);
            }


        } catch (Exception e) {
            log.error("error processing sms", e);
            if (sms != null) {
                sms.status = SmsStatus.FAILED;
                sms.statusComments = e.getMessage();
            }
        }


        try {
            smsService.update(sms);
        } catch (Exception e) {
            log.error("error saving ");
        }

    }
}
