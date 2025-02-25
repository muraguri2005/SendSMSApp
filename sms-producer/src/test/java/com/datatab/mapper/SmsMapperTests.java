package com.datatab.mapper;

import com.datatab.controller.dto.SmsDto;
import com.datatab.domain.Sms;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {SmsMapperImpl.class})
public class SmsMapperTests {
    @Autowired
    SmsMapper smsMapper;

    @Test
    void smsToSmsDtoTest() {
        var sms = new Sms();
        sms.message = "Hello World";
        sms.recipient = "+254700000001";
        var smsDto = smsMapper.smsToSmsDto(sms);
        Assertions.assertEquals(sms.message, smsDto.message);
        Assertions.assertEquals(sms.message, smsDto.message);
    }

    @Test
    void smsDtoToSmsTest() {
        var smsDto = new SmsDto();
        smsDto.message = "Welcome World";
        smsDto.recipient = "+254700100000";
        var sms = smsMapper.smsDtoToSms(smsDto);
        Assertions.assertEquals(sms.message, smsDto.message);
        Assertions.assertEquals(sms.message, smsDto.message);
    }
}
