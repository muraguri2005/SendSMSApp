package com.datatab.controller;

import com.datatab.controller.dto.SmsDto;
import com.datatab.service.SmsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;


@RestController
@RequestMapping("/sms")
public class SmsController {
    final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<SmsDto> saveSms(@RequestBody @Validated SmsDto smsDto, @AuthenticationPrincipal Principal principal) {
        return Mono.just(smsService.create(smsDto, principal));
    }

    @GetMapping
    Mono<Page<SmsDto>> getSmsList(Pageable pageable) {
        return Mono.just(smsService.findAll(pageable));
    }
}
