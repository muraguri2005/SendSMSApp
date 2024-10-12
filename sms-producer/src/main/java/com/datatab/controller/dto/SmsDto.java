package com.datatab.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class SmsDto {
    @NotBlank(message = "message is required")
    public String message;
    @NotBlank(message = "recipient is required")
    public String recipient;
    public String sender;
}
