package com.datatab.controller;

import com.datatab.controller.dto.SmsDto;
import com.datatab.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/sms")
public class SmsController {
    final SmsService smsService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    SmsDto saveSms(@RequestBody @Validated SmsDto smsDto, @AuthenticationPrincipal Principal principal) {
        return smsService.create(smsDto, principal);
    }

    @GetMapping
    Page<SmsDto> getSmsList(Pageable pageable) {
        return smsService.findAll(pageable);
    }
}
