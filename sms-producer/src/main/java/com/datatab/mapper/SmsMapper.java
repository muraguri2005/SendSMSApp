package com.datatab.mapper;

import com.datatab.controller.dto.SmsDto;
import com.datatab.domain.Sms;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SmsMapper {
    Sms smsDtoToSms(SmsDto smsDto);
}
