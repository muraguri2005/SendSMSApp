package org.freelesson.sendsms.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.freelesson.sendsms.domain.Sms;
import org.freelesson.sendsms.domain.enums.SmsStatus;
import org.freelesson.sendsms.exception.BaseException;
import org.freelesson.sendsms.repository.SmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.africastalking.AfricasTalking;
import com.africastalking.SmsService;
import com.africastalking.sms.Recipient;

@Service
public class SendSMSServiceImpl {
	String username="sandbox";
	String apiKey="4dde814dcaeb22146f2ec9be84c4a5678c275cdb98b80c3a2920b2dd339044bb";
	@Autowired
	org.freelesson.sendsms.service.SmsService smsService;
	
	@PostConstruct
    public void init() {
        AfricasTalking.initialize(username, apiKey);
    }
	
	@Autowired
	SmsRepository smsRepository;
	@Scheduled(fixedDelay=60000)
	public void sendSMSFromQueue() {
		Pageable pageable=PageRequest.of(0, 5);
		List<Sms> unSentSmsList = smsRepository.findByStatusAndTransmissionTimeLessThan(SmsStatus.QUEUED, new Date(), pageable);
		System.out.println("Sms to send "+unSentSmsList.size());
		if (unSentSmsList.size()==0)
			return;
		for(Sms sms: unSentSmsList) {
			SmsService atSmsService=AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
			try {
				List<Recipient> response = atSmsService.send(sms.message, "RMG" ,new String[] {sms.recepient}, true);
				sms.status = SmsStatus.SENT;
				System.out.println(response);
				if (response.size()>0) {
					Recipient recipient=response.get(0);
					sms.externalId = recipient.messageId;
					try {
						sms.cost = Double.parseDouble(recipient.cost);
					}catch (NumberFormatException e) {
						
					}
					if (!recipient.status.equals("Success")) {
						sms.status = SmsStatus.FAILED;
						sms.statusComments = recipient.status;
					}
				}
			} catch (IOException e) {
				sms.status = SmsStatus.FAILED;
				sms.statusComments = e.getMessage();
				e.printStackTrace();
			}
			try {
				smsService.update(sms);
			} catch (BaseException e) {
				e.printStackTrace();
			}
		}
	}
}
