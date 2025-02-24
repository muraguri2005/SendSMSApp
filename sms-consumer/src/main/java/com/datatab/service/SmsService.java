package com.datatab.service;

import com.datatab.domain.Sms;
import com.datatab.exception.BaseException;
import com.datatab.exception.ObjectNotFoundException;
import com.datatab.repository.SmsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SmsService {
    final SmsRepository smsRepository;

    public SmsService(SmsRepository smsRepository) {
        this.smsRepository = smsRepository;
    }

    public Sms create(Sms sms) {
        return smsRepository.save(sms);
    }


    public Sms update(Sms sms) throws BaseException {
        smsRepository.findById(sms.id).orElseThrow(() -> new ObjectNotFoundException("sms not found"));
        return smsRepository.save(sms);
    }


    public Optional<Sms> findByExternalId(String externalId) {
        return smsRepository.findByExternalId(externalId);
    }

}
