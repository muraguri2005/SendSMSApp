package org.freelesson.sendsms.domain.controller;

import java.util.Date;

import org.freelesson.sendsms.domain.Sms;
import org.freelesson.sendsms.domain.enums.SmsStatus;
import org.freelesson.sendsms.service.SmsService;
import org.freelesson.sendsms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {
	@Autowired
	SmsService smsService;
	@Autowired
	UserService userService;
	@PostMapping
	Sms saveSms(@RequestBody Sms sms, @AuthenticationPrincipal String username) {
		sms.status = SmsStatus.QUEUED;
		sms.createdOn = new Date();
		sms.createdBy = userService.findByUsername(username).get().id;
		sms.sender = "RMG";
		if (sms.transmissionTime==null) {
			sms.transmissionTime = new Date();
		}
		return smsService.create(sms);
	}
	@GetMapping
	Page<Sms> getSmsList(Pageable pageable) {
		return smsService.findAll(pageable);
	}
}
