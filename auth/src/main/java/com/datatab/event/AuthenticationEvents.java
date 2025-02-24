package com.datatab.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthenticationEvents {
    Logger logger = LoggerFactory.getLogger(AuthenticationEvents.class);

    @EventListener
    void onSuccess(AuthenticationSuccessEvent event) {
        log(event);
    }

    @EventListener
    void onFailure(AbstractAuthenticationFailureEvent event) {
        log(event);
    }

    void log(AbstractAuthenticationEvent event) {
        if (event instanceof AbstractAuthenticationFailureEvent) {
            logger.info("Failed authentication attempt at:{}", LocalDateTime.now());
            logger.error("Exception: ", ((AbstractAuthenticationFailureEvent) event).getException());
        } else {
            logger.info("Successful authentication attempt at: {}", LocalDateTime.now());
        }
        logger.info("User: {}", event.getAuthentication().getName());
        logger.info("Authorities: {}", event.getAuthentication().getAuthorities());
        logger.info("Details: {}", event.getAuthentication().getDetails());
        logger.info("Credentials: {}", event.getAuthentication().getCredentials());
        logger.info("Principal: {}", event.getAuthentication().getPrincipal());
        logger.info("Is authenticated: {}", event.getAuthentication().isAuthenticated());
    }
}
