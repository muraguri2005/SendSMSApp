package org.freelesson.sendsms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.freelesson.sendsms.domain.Role;
import org.freelesson.sendsms.domain.User;
import org.freelesson.sendsms.domain.util.SpringUser;
import org.freelesson.sendsms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;
	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user =	userRepository.findByUsername(username).orElseThrow(() ->  new UsernameNotFoundException("Wrong username or password"));
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("USER"));
		SpringUser springUser= new SpringUser(username, user.password, user.isEnabled, true, true, true, getAuthorities(user.roles));
		return springUser;
	}
	
	List<SimpleGrantedAuthority> getAuthorities(Set<Role> roles) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		roles.stream().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.name));
		});
		return authorities;
	}

}
