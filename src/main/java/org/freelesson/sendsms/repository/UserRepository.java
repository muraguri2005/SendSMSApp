package org.freelesson.sendsms.repository;

import java.util.Optional;

import org.freelesson.sendsms.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUsername(String username);
}
