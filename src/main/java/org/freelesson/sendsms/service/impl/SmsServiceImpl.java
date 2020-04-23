package org.freelesson.sendsms.service.impl;

import java.util.Optional;

import org.freelesson.sendsms.domain.Sms;
import org.freelesson.sendsms.exception.BaseException;
import org.freelesson.sendsms.exception.ObjectNotFoundException;
import org.freelesson.sendsms.repository.SmsRepository;
import org.freelesson.sendsms.service.SmsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {
	final SmsRepository smsRepository;

	public SmsServiceImpl(SmsRepository smsRepository) {
		this.smsRepository = smsRepository;
	}

	@Override
	public Sms create(Sms sms) {
		return smsRepository.save(sms);
	}
	
	@Override
	public Sms update(Sms sms) throws BaseException {
		smsRepository.findById(sms.id).orElseThrow(() -> new ObjectNotFoundException("sms not found"));
		return smsRepository.save(sms);
	}
	
	@Override
	public Optional<Sms> findByExternalId(String externalId) {
		return smsRepository.findByExternalId(externalId);
	}
	
	@Override
	public Page<Sms> findAll(Pageable pageable) {
		return smsRepository.findAll(pageable);
	}
	
}
