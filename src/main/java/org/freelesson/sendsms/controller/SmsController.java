package org.freelesson.sendsms.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.freelesson.sendsms.domain.Sms;
import org.freelesson.sendsms.domain.enums.SmsStatus;
import org.freelesson.sendsms.exception.ObjectNotFoundException;
import org.freelesson.sendsms.service.SmsService;
import org.freelesson.sendsms.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@RestController
@RequestMapping("/sms")
public class SmsController {
    final SmsService smsService;
    final UserService userService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public SmsController(SmsService smsService, UserService userService) {
        this.smsService = smsService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Sms saveSms(@RequestBody Sms sms, @AuthenticationPrincipal String username) throws Exception {
        log.info("saving sms to queue");
        sms.status = SmsStatus.QUEUED;
        sms.createdOn = new Date();
        sms.createdBy = userService.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("user not found")).id;
        sms.sender = "RMG";
        if (sms.transmissionTime == null) {
            sms.transmissionTime = new Date();
        }
        Sms savedSms = smsService.create(sms);
        log.info("sms saved to queue with id {}", savedSms.id);
        return savedSms;
    }

    @GetMapping
    Page<Sms> getSmsList(Pageable pageable) {
        return smsService.findAll(pageable);
    }

    @Operation(description = "", hidden = true)
    @PostMapping("/processdeliveryreport")
    @ResponseStatus(HttpStatus.OK)
    void processDeliveryResponse(HttpServletRequest request) throws Exception {
        log.info("received sms delivery report from {} with id {}, status {} and failureReason {}", request.getRemoteAddr(), request.getParameter("id"), request.getParameter("status"), request.getParameter("failureReason"));
        Sms sms = smsService.findByExternalId(request.getParameter("id")).orElseThrow(() -> new ObjectNotFoundException("sms not found"));
        sms.status = SmsStatus.valueOf(request.getParameter("status"));
        if (request.getParameter("failureReason") != null && !request.getParameter("failureReason").isEmpty())
            sms.statusComments = request.getParameter("failureReason");
        smsService.update(sms);
        log.info("sms with id {} updated successfully", sms.id);
    }
}
