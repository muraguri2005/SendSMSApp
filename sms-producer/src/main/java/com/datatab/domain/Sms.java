package com.datatab.domain;

import com.datatab.domain.enums.SmsStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;


@Entity
@Table(name = "sms")
public class Sms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    public Long id;

    @NotNull(message = "message is required")
    @Column(nullable = false)
    public String message;

    //phone number with country code prefix
    @NotNull(message = "recepient is required")
    @Column(nullable = false)
    public String recepient;

    //shortcode or alphanumeric
    @NotNull(message = "sender is required")
    @Column(nullable = false)
    public String sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "status is required")
    public SmsStatus status;

    public String statusComments;

    public String externalId;

    @Column(nullable = false)
    @NotNull(message = "createdOn is required")
    public Date createdOn;

    @Column(nullable = false)
    @NotNull(message = "createdBy is required")
    public Long createdBy;

    @Column(nullable = false)
    @NotNull(message = "transmissionTime is required")
    public Date transmissionTime;

    @Column(nullable = false)
    public double cost = 0;
}
