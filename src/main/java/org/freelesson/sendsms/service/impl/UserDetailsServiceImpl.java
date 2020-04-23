package org.freelesson.sendsms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.freelesson.sendsms.domain.Role;
import org.freelesson.sendsms.domain.User;
import org.freelesson.sendsms.domain.util.SpringUser;
import org.freelesson.sendsms.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user =	userRepository.findByUsername(username).orElseThrow(() ->  new UsernameNotFoundException("Wrong username or password"));
		return new SpringUser(username, user.password, user.isEnabled, true, true, true, getAuthorities(user.roles));
	}
	
	List<SimpleGrantedAuthority> getAuthorities(Set<Role> roles) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name)));
		return authorities;
	}

}
