package com.datatab.service;

import com.datatab.config.CustomMapper;
import com.datatab.controller.dto.SmsDto;
import com.datatab.mapper.SmsMapper;
import com.datatab.mapper.SmsMapperImpl;
import com.datatab.properties.SmsProducerProperties;
import com.datatab.repository.SmsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SmsService.class, SmsRepository.class, CustomMapper.class})
@Import({SmsMapperImpl.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SmsServiceTests {
    @MockitoBean
    SmsRepository smsRepository;
    @Autowired
    SmsService smsService;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    SmsProducerProperties smsProducerProperties;
    @MockitoBean
    KafkaTemplate<String, String> kafkaTemplate;
    @Mock
    Principal principal;
    SmsDto smsDto = new SmsDto();
    @Autowired
    private SmsMapper smsMapper;

    @BeforeAll
    void setUp() {
        when(principal.getName()).thenReturn("admin");
        smsDto.message = "New world is coming";
        smsDto.recipient = "+254100100000";
        var mockSms = smsMapper.smsDtoToSms(smsDto);
        when(smsRepository.save(any())).thenReturn(mockSms);
    }

    @Test
    void testCreateSms() {
        var created = smsService.create(smsDto, principal);
        Assertions.assertEquals(created.message, smsDto.message);
        Assertions.assertEquals(created.recipient, smsDto.recipient);
    }
}
