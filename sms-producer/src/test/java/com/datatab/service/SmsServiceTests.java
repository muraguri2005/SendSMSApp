package com.datatab.service;

import com.datatab.config.CustomMapper;
import com.datatab.controller.dto.SmsDto;
import com.datatab.domain.Sms;
import com.datatab.mapper.SmsMapper;
import com.datatab.mapper.SmsMapperImpl;
import com.datatab.properties.SmsProducerProperties;
import com.datatab.repository.SmsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Principal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SmsService.class, SmsRepository.class, CustomMapper.class})
@Import({SmsMapperImpl.class})
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

    @BeforeEach
    void setUp() {
        when(principal.getName()).thenReturn("admin");
        smsDto.message = "New world is coming";
        smsDto.recipient = "+254100100000";
        smsDto.id = 1L;
        var mockSms = smsMapper.smsDtoToSms(smsDto);
        mockSms.id = 1L;
        System.out.println(mockSms.id);
        when(smsRepository.save(any())).thenReturn(mockSms);
        when(smsRepository.findAll(Pageable.ofSize(1))).thenReturn(new PageImpl<>(List.of(new Sms()), Pageable.ofSize(1), 1));
    }

    @Test
    void testSmsFunctionality() {
        var created = smsService.create(smsDto, principal);
        Assertions.assertEquals(created.message, smsDto.message);
        Assertions.assertEquals(created.recipient, smsDto.recipient);
    }

    @Test
    void testFindAll() {
        var smsList = smsService.findAll(Pageable.ofSize(1));
        Assertions.assertFalse(smsList.isEmpty());
    }
}
