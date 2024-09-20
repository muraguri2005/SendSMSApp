package com.datatab.service;

import com.africastalking.AfricasTalking;
import com.africastalking.sms.Recipient;
import com.datatab.domain.Sms;
import com.datatab.domain.enums.SmsStatus;
import com.datatab.exception.BaseException;
import com.datatab.properties.SmsConsumerProperties;
import com.datatab.repository.SmsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SendSMSService {
    final SmsService smsService;
    final SmsRepository smsRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public SendSMSService(SmsService smsService, SmsRepository smsRepository, SmsConsumerProperties smsConsumerProperties) {
        AfricasTalking.initialize(smsConsumerProperties.africasTalkingUsername(), smsConsumerProperties.africasTalkingPassword());
        this.smsService = smsService;
        this.smsRepository = smsRepository;
    }

    @KafkaListener(topics = "${com.datatab.send-sms-queue-name}", groupId = "${com.datatab.send-sms-queue-group-id}")
    public void sendSMSFromQueue(Sms sms) {
        com.africastalking.SmsService atSmsService = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        try {
            log.info("sending sms via africastalking");
            List<Recipient> response = atSmsService.send(sms.message, "RMG", new String[]{sms.recepient}, true);
            sms.status = SmsStatus.SENT;
            log.info("sending sms response {}", response);
            if (!response.isEmpty()) {
                Recipient recipient = response.getFirst();
                sms.externalId = recipient.messageId;
                try {
                    sms.cost = Double.parseDouble(recipient.cost.split(" ")[1]);
                } catch (Exception e) {
                    log.error("error getting cost {}", e.getMessage());
                }
                if (!recipient.status.equals("Success")) {
                    sms.status = SmsStatus.FAILED;
                    sms.statusComments = recipient.status;
                }
            }
        } catch (IOException e) {
            sms.status = SmsStatus.FAILED;
            sms.statusComments = e.getMessage();

            log.error("error sending sms", e);
        }
        try {
            smsService.update(sms);
        } catch (BaseException e) {
            log.error("error updating sms", e);
        }

    }
}
