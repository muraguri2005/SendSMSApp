package org.freelesson.sendsms.service;

import org.freelesson.sendsms.domain.Sms;
import org.freelesson.sendsms.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SmsService {
    Sms create(Sms sms);

    Sms update(Sms sms) throws BaseException;

    Optional<Sms> findByExternalId(String externalId);

    Page<Sms> findAll(Pageable pageable);

}
