package org.freelesson.sendsms.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.freelesson.sendsms.domain.Sms;
import org.freelesson.sendsms.domain.enums.SmsStatus;
import org.freelesson.sendsms.exception.BaseException;
import org.freelesson.sendsms.repository.SmsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	final org.freelesson.sendsms.service.SmsService smsService;
	final  SmsRepository smsRepository;
	private final  Logger log = LoggerFactory.getLogger(this.getClass());
	
	@PostConstruct
    public void init() {
        AfricasTalking.initialize(username, apiKey);
    }

    public SendSMSServiceImpl(org.freelesson.sendsms.service.SmsService smsService,SmsRepository smsRepository) {
		this.smsService = smsService;
		this.smsRepository = smsRepository;
	}

	@Scheduled(fixedDelay=60000, initialDelay = 180000)
	public void sendSMSFromQueue() {
		Pageable pageable=PageRequest.of(0, 5);
		List<Sms> unSentSmsList = smsRepository.findByStatusAndTransmissionTimeLessThan(SmsStatus.QUEUED, new Date(), pageable);
		log.info("Number of SMS(es) to send {}",unSentSmsList.size());
		if (unSentSmsList.size()==0)
			return;
		for(Sms sms: unSentSmsList) {
			SmsService atSmsService=AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
			try {
				log.info("sending sms via africastalking");
				List<Recipient> response = atSmsService.send(sms.message, "RMG" ,new String[] {sms.recepient}, true);
				sms.status = SmsStatus.SENT;
				log.info("sending sms response {}",response);
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
				log.error("error sending sms {}",e.getMessage());
			}
			try {
				smsService.update(sms);
			} catch (BaseException e) {
				log.error("error updating sms {}",e.getMessage());
			}
		}
	}
}
