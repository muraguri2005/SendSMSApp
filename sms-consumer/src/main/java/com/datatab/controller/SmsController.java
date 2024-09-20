package com.datatab.controller;

import com.datatab.domain.Sms;
import com.datatab.domain.enums.SmsStatus;
import com.datatab.exception.ObjectNotFoundException;
import com.datatab.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sms")
public class SmsController {
    final SmsService smsService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @Operation(description = "", hidden = true)
    @PostMapping("/processdeliveryreport")
    @ResponseStatus(HttpStatus.OK)
    void processDeliveryResponse(ServerHttpRequest request) throws Exception {
        log.info("received sms delivery report from {} with id {}, status {} and failureReason {}", request.getRemoteAddress(), request.getQueryParams().get("id"), request.getQueryParams().get("status"), request.getQueryParams().get("failureReason"));
        Sms sms = smsService.findByExternalId(request.getQueryParams().get("id").getFirst()).orElseThrow(() -> new ObjectNotFoundException("sms not found"));
        sms.status = SmsStatus.valueOf(request.getQueryParams().get("status").getFirst());
        if (request.getQueryParams().get("failureReason") != null && !request.getQueryParams().get("failureReason").isEmpty())
            sms.statusComments = request.getQueryParams().get("failureReason").getFirst();
        smsService.update(sms);
        log.info("sms with id {} updated successfully", sms.id);
    }
}
