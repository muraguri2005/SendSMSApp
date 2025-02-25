package com.datatab.controller;

import com.datatab.controller.dto.SmsDto;
import com.datatab.exception.BaseExceptionHandler;
import com.datatab.service.SmsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SmsControllerTests {
    WebTestClient webTestClient;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeAll
    void setUp() {
        SmsService smsService = mock(SmsService.class);
        when(smsService.create(any(), any())).thenReturn(new SmsDto());
        when(smsService.findAll(any())).thenReturn(new PageImpl<>(List.of(new SmsDto()), Pageable.ofSize(1), 1));
        webTestClient = WebTestClient.bindToController(new SmsController(smsService))
                .argumentResolvers(argumentResolverConfigurer -> argumentResolverConfigurer.addCustomResolver(new ReactivePageableHandlerMethodArgumentResolver()))
                .controllerAdvice(new BaseExceptionHandler())
                .build();
    }

    @Test
    void whenMessageIsBlankOrNullThenBadRequest() throws JsonProcessingException {
        var smsDto = new SmsDto();
        smsDto.recipient = "+254700000001";
        webTestClient.post().uri("/sms")
                .headers(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON))
                .body(Mono.just(objectMapper.writeValueAsString(smsDto)), String.class).exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenRecipientIsBlankOrNullThenBadRequest() throws JsonProcessingException {
        var smsDto = new SmsDto();
        smsDto.message = "Go Home";
        webTestClient.post().uri("/sms")
                .headers(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON))
                .body(Mono.just(objectMapper.writeValueAsString(smsDto)), String.class).exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenDataIsValidThenSuccess() throws JsonProcessingException {
        var smsDto = new SmsDto();
        smsDto.message = "Go Home";
        smsDto.recipient = "+254700000001";
        webTestClient.post().uri("/sms")
                .headers(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON))
                .body(Mono.just(objectMapper.writeValueAsString(smsDto)), String.class).exchange()
                .expectStatus().is2xxSuccessful();
        webTestClient.get().uri("/sms")
                .headers(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON))
                .exchange().expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.totalElements").isNotEmpty()
                .jsonPath("$.totalElements").isEqualTo("1");
    }
}
