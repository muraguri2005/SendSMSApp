package com.datatab.repository;

import com.datatab.domain.Sms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsRepository extends JpaRepository<Sms, Long> {
    Optional<Sms> findByExternalId(String externalId);
}
