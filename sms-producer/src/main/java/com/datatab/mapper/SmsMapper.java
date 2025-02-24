package com.datatab.mapper;

import com.datatab.controller.dto.SmsDto;
import com.datatab.domain.Sms;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class SmsMapper {
    public abstract Sms smsDtoToSms(SmsDto smsDto);

    public abstract SmsDto smsToSmsDto(Sms sms);
}
