package com.datatab.controller;

import com.datatab.controller.dto.SmsDto;
import com.datatab.service.SmsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SmsControllerTests {
    WebTestClient webTestClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    void setUp() {
        SmsService smsService = mock(SmsService.class);
        webTestClient = WebTestClient.bindToController(new SmsController(smsService)).build();
    }

    @Test
    void whenMessageIsBlankOrNullThenBadRequest() throws JsonProcessingException {
        var smsDto = new SmsDto();
        smsDto.recipient = "+254700000001";
        webTestClient.post().uri("/sms").headers(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON)).body(Mono.just(objectMapper.writeValueAsString(smsDto)), String.class).exchange().expectStatus().isBadRequest();
    }

    @Test
    void whenRecipientIsBlankOrNullThenBadRequest() throws JsonProcessingException {
        var smsDto = new SmsDto();
        smsDto.message = "Go Home";
        webTestClient.post().uri("/sms").headers(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON)).body(Mono.just(objectMapper.writeValueAsString(smsDto)), String.class).exchange().expectStatus().isBadRequest();
    }
}
