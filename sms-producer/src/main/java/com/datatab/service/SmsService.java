package com.datatab.service;

import com.datatab.domain.Sms;
import com.datatab.repository.SmsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    final SmsRepository smsRepository;

    public SmsService(SmsRepository smsRepository) {
        this.smsRepository = smsRepository;
    }


    public Sms create(Sms sms) {
        return smsRepository.save(sms);
    }


    public Page<Sms> findAll(Pageable pageable) {
        return smsRepository.findAll(pageable);
    }

}
