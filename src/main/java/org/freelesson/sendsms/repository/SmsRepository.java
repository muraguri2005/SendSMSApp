package org.freelesson.sendsms.repository;

import org.freelesson.sendsms.domain.Sms;
import org.freelesson.sendsms.domain.enums.SmsStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SmsRepository extends JpaRepository<Sms, Long> {
    Optional<Sms> findByExternalId(String externalId);

    List<Sms> findByStatusAndTransmissionTimeLessThan(SmsStatus smsStatus, Date transmissionTime, Pageable pageable);
}
