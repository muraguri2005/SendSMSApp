package org.freelesson.sendsms.service;

import org.freelesson.sendsms.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
}
