package com.datatab.service;


import com.datatab.domain.User;
import com.datatab.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserAuthenticationService implements UserDetailsService {
    final UserRepository userRepository;

    public UserAuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Wrong username or password"));
        var authorities = user.roles.stream().map(role -> new SimpleGrantedAuthority(role.name)).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.username, user.password, authorities);
    }
}
