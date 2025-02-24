package com.datatab.service;

import com.datatab.controller.dto.SmsDto;
import com.datatab.domain.Sms;
import com.datatab.domain.enums.SmsStatus;
import com.datatab.mapper.SmsMapper;
import com.datatab.properties.SmsProducerProperties;
import com.datatab.repository.SmsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

@Service
public class SmsService {
    final SmsRepository smsRepository;
    final SmsMapper smsMapper;
    final ObjectMapper objectMapper;
    final SmsProducerProperties smsProducerProperties;
    final KafkaTemplate<String, String> kafkaTemplate;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public SmsService(SmsRepository smsRepository, SmsMapper smsMapper, ObjectMapper objectMapper, SmsProducerProperties smsProducerProperties, KafkaTemplate<String, String> kafkaTemplate) {
        this.smsRepository = smsRepository;
        this.smsMapper = smsMapper;
        this.objectMapper = objectMapper;
        this.smsProducerProperties = smsProducerProperties;
        this.kafkaTemplate = kafkaTemplate;
    }


    public SmsDto create(SmsDto smsDto, Principal principal) {
        Sms sms = smsMapper.smsDtoToSms(smsDto);
        sms.status = SmsStatus.QUEUED;
        sms.createdOn = new Date();
        sms.createdBy = principal.getName();
        sms.sender = "RMG";
        if (sms.transmissionTime == null) {
            sms.transmissionTime = new Date();
        }
        try {
            log.info("saving sms to queue {}", objectMapper.writeValueAsString(sms));
        } catch (JsonProcessingException ignored) {

        }
        Sms savedSms = smsRepository.save(sms);
        log.info("sms saved to queue with id {}", savedSms.id);
        try {
            kafkaTemplate.send(smsProducerProperties.sendSmsQueueName(), objectMapper.writeValueAsString(sms));
        } catch (JsonProcessingException ignored) {
        }
        return smsMapper.smsToSmsDto(savedSms);
    }


    public Page<SmsDto> findAll(Pageable pageable) {
        return smsRepository.findAll(pageable).map(smsMapper::smsToSmsDto);
    }

}
