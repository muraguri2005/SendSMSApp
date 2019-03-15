package org.freelesson.sendsms.service;

import java.util.Optional;

import org.freelesson.sendsms.domain.User;

public interface UserService {
	Optional<User> findByUsername(String username);
}
