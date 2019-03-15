package org.freelesson.sendsms.repository;

import java.util.Optional;

import org.freelesson.sendsms.domain.Role;
import org.springframework.data.repository.CrudRepository;


public interface RoleRepository extends CrudRepository<Role, Long>{
	Optional<Role> findByName(String name);
}
