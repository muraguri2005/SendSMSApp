package org.freelesson.sendsms.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.freelesson.sendsms.domain.Sms;
import org.freelesson.sendsms.domain.enums.SmsStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<Sms, Long>{
	Optional<Sms> findByExternalId(String externalId);
	List<Sms> findByStatusAndTransmissionTimeLessThan(SmsStatus smsStatus,Date transmissionTime, Pageable pageable);
}
