package org.freelesson.sendsms.repository;

import org.freelesson.sendsms.domain.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
