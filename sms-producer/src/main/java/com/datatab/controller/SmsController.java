package com.datatab.controller;

import com.datatab.controller.dto.SmsDto;
import com.datatab.domain.Sms;
import com.datatab.domain.enums.SmsStatus;
import com.datatab.mapper.SmsMapper;
import com.datatab.properties.SmsProducerProperties;
import com.datatab.service.SmsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;


@RestController
@RequestMapping("/sms")
public class SmsController {
    final SmsService smsService;
    final KafkaTemplate<String, String> kafkaTemplate;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;
    private final SmsProducerProperties smsProducerProperties;
    private final SmsMapper smsMapper;

    public SmsController(SmsService smsService, KafkaTemplate<String, String> kafkaTemplate, @Qualifier("objectMapper") ObjectMapper objectMapper, SmsProducerProperties smsProducerProperties, SmsMapper smsMapper) {
        this.smsService = smsService;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.smsProducerProperties = smsProducerProperties;
        this.smsMapper = smsMapper;
    }

    //TODO: use a dto here
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Sms saveSms(@RequestBody @Validated SmsDto smsDto, @AuthenticationPrincipal Principal principal) {
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
        Sms savedSms = smsService.create(sms);
        log.info("sms saved to queue with id {}", savedSms.id);
        try {
            kafkaTemplate.send(smsProducerProperties.sendSmsQueueName(), objectMapper.writeValueAsString(sms));
        } catch (JsonProcessingException ignored) {
        }
        return savedSms;
    }

    @GetMapping
    Page<Sms> getSmsList(Pageable pageable) {
        return smsService.findAll(pageable);
    }
}
