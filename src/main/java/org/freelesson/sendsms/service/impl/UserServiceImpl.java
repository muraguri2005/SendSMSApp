package org.freelesson.sendsms.service.impl;

import java.util.Optional;

import org.freelesson.sendsms.domain.User;
import org.freelesson.sendsms.repository.UserRepository;
import org.freelesson.sendsms.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	final UserRepository userRepository;
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
}
